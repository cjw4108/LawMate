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

    // 매번 생성하지 않도록 상단에 선언
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);
        sessionRoomMap.put(session.getId(), roomId);
        System.out.println("[접속] roomId=" + roomId + " | 현재 세션수: " + rooms.get(roomId).size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            Map<String, Object> data = mapper.readValue(payload, Map.class);

            String roomId = (String) data.get("roomId");
            String userId = (String) data.get("userId");
            String userMessage = (String) data.get("message");
            String chatWith = (String) data.get("chatWith");
            String senderType = (data.get("senderType") != null) ? (String) data.get("senderType") : "USER";

            // 1️⃣ [공통 로직] 일단 받은 메시지는 무조건 DB에 저장 (나중에 불러오기 위함)
            CompletableFuture.runAsync(() -> {
                try {
                    // senderId는 보낸 사람의 ID(userId), senderType은 USER 또는 LAWYER
                    chattingService.saveMessage(roomId, userId, senderType, userMessage);
                    System.out.println("✅ DB 저장 완료 (" + senderType + "): " + userMessage);
                } catch (Exception e) {
                    System.err.println("❌ 메시지 DB 저장 실패: " + e.getMessage());
                }
            });

            // 2️⃣ [분기 로직] AI 상담 vs 변호사 상담
            if ("AI".equals(chatWith)) {
                // Gemini API 호출 및 응답 전송 로직 (기존 유지)
                String aiAnswer = chattingService.askGemini(userMessage);
                sendResponse(session, roomId, "AI", "AI상담사", aiAnswer);

                // AI 답변도 DB에 저장
                CompletableFuture.runAsync(() -> {
                    chattingService.saveMessage(roomId, "GEMINI_AI", "AI", aiAnswer);
                });
            } else {
                // ⚖️ 변호사 모드: 내가 보낸 메시지를 상대방(변호사 또는 다른 유저)에게 실시간 전달
                // 위에서 이미 saveMessage를 실행했으므로 여기서는 전달(broadcast)만 수행
                broadcast(roomId, new TextMessage(payload), session);
            }

        } catch (Exception e) {
            System.err.println("🚨 핸들러 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 상대방에게 메시지 전달 (나 제외)
    private void broadcast(String roomId, TextMessage message, WebSocketSession senderSession) {
        if (rooms.containsKey(roomId)) {
            rooms.get(roomId).forEach(ws -> {
                try {
                    if (ws.isOpen() && !ws.getId().equals(senderSession.getId())) {
                        ws.sendMessage(message);
                    }
                } catch (Exception e) {
                    System.err.println("❌ 전송 실패: " + e.getMessage());
                }
            });
        }
    }

    // 응답 전송 공통 메서드 (AI용)
    private void sendResponse(WebSocketSession session, String roomId, String type, String name, String msg) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("senderType", type);
        response.put("senderName", name);
        response.put("message", msg);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = sessionRoomMap.get(session.getId());
        if (roomId != null && rooms.containsKey(roomId)) {
            rooms.get(roomId).remove(session);
            if (rooms.get(roomId).isEmpty()) rooms.remove(roomId);
        }
        sessionRoomMap.remove(session.getId());
        System.out.println("[종료] 세션 ID: " + session.getId());
    }

    private String getRoomId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("roomId=")) {
            return query.split("roomId=")[1].split("&")[0];
        }
        return "defaultRoom";
    }
}