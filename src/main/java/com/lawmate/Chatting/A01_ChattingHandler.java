package com.lawmate.Chatting;

import java.util.*;
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

    // 2️⃣ 메시지 처리 (수정됨)
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(payload, Map.class);

        String roomId = (String) data.get("roomId");
        String userId = (String) data.get("userId");
        String userMessage = (String) data.get("message");
        String chatWith = (String) data.get("chatWith"); // "AI" 또는 "LAWYER"

        // 🔥 프론트에서 보낸 senderType을 가져오되, 없을 경우 기본값 "USER" 설정
        String senderType = (data.get("senderType") != null) ? (String) data.get("senderType") : "USER";

        if ("AI".equals(chatWith)) {
            // 🤖 AI 상담 모드
            // 유저 질문 저장 (선택 사항, 필요 시 추가)
            chattingService.saveMessage(roomId, userId, "USER", userMessage);

            String aiAnswer = chattingService.askGemini(userMessage);

            // AI 답변 저장 및 전송
            chattingService.saveMessage(roomId, "GEMINI_AI", "AI", aiAnswer);
            sendResponse(session, roomId, "AI", "AI상담사", aiAnswer);

        } else {
            // ⚖️ 변호사 상담 모드
            // 1. DB 저장 (한 번만 호출)
            // senderType은 JSP에서 "USER" 또는 "LAWYER"로 보내주어야 합니다.
            chattingService.saveMessage(roomId, userId, senderType, userMessage);

            // 2. 상대방에게 실시간 브로드캐스트
            broadcast(roomId, new TextMessage(payload), session);
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