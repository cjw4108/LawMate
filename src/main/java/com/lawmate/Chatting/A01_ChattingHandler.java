package com.lawmate.Chatting;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component("chatHandler")
public class A01_ChattingHandler extends TextWebSocketHandler {

    @Autowired
    private A03_ChattingService chattingService;

    private Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    // 1️⃣ 접속 (동일)
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);
        sessionRoomMap.put(session.getId(), roomId);
        System.out.println("[접속] roomId=" + roomId);
    }

    // 2️⃣ 메시지 처리 (수정 완료)
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> data = mapper.readValue(payload, Map.class);

            String roomId = (String) data.get("roomId");
            String userId = (String) data.get("userId");
            String userMessage = (String) data.get("message");
            String chatWith = (String) data.get("chatWith");
            String senderType = (data.get("senderType") != null) ? (String) data.get("senderType") : "USER";

            System.out.println("📩 메시지 수신 - 방ID: " + roomId + ", 유저ID: " + userId);

            if ("AI".equals(chatWith)) {
                // 1. 유저 질문 저장 (예외 처리 추가)
                CompletableFuture.runAsync(() -> {
                    try {
                        chattingService.saveMessage(roomId, userId, "USER", userMessage);
                    } catch (Exception e) {
                        System.err.println("❌ 유저 메시지 저장 실패: " + e.getMessage());
                    }
                });

                // 2. Gemini API 호출
                String aiAnswer = chattingService.askGemini(userMessage);

                // 3. AI 답변 전송
                sendResponse(session, roomId, "AI", "AI상담사", aiAnswer);

                // 4. AI 답변 저장 (final 변수 처리)
                final String finalAiAnswer = aiAnswer;
                CompletableFuture.runAsync(() -> {
                    try {
                        chattingService.saveMessage(roomId, "GEMINI_AI", "AI", finalAiAnswer);
                    } catch (Exception e) {
                        System.err.println("❌ AI 메시지 저장 실패: " + e.getMessage());
                    }
                });

            } else {
                // ⚖️ 변호사 모드 저장
                CompletableFuture.runAsync(() -> {
                    try {
                        chattingService.saveMessage(roomId, userId, senderType, userMessage);
                    } catch (Exception e) {
                        System.err.println("❌ 변호사 모드 메시지 저장 실패: " + e.getMessage());
                    }
                });
                broadcast(roomId, new TextMessage(payload), session);
            }
        } catch (Exception e) {
            System.err.println("🚨 핸들러 전체 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 응답 전송 공통 메서드
    private void sendResponse(WebSocketSession session, String roomId, String type, String name, String msg) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("senderType", type);
        response.put("senderName", name);
        response.put("message", msg);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
    }

    private void broadcast(String roomId, TextMessage message, WebSocketSession senderSession) {
        if (rooms.containsKey(roomId)) {
            rooms.get(roomId).forEach(ws -> {
                try {
                    // 핵심: 현재 루프를 도는 세션(ws)이 메시지를 보낸 사람(senderSession)이 아닐 때만 전송
                    if (!ws.getId().equals(senderSession.getId())) {
                        ws.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // 3️⃣ 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String roomId = sessionRoomMap.get(session.getId());

        if (roomId != null && rooms.containsKey(roomId)) {
            rooms.get(roomId).remove(session);

            if (rooms.get(roomId).isEmpty()) {
                rooms.remove(roomId);
            }
        }

        sessionRoomMap.remove(session.getId());
    }

    private String getRoomId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("roomId=")) {
            return query.split("=")[1];
        }
        return "defaultRoom";
    }
}