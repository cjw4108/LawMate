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

    // 1️⃣ 접속
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String roomId = getRoomId(session);

        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);

        sessionRoomMap.put(session.getId(), roomId);

        System.out.println("[접속] roomId=" + roomId);
    }

    // 2️⃣ 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        System.out.println("[메시지 수신] " + payload);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(payload, Map.class);

        String roomId = (String) data.get("roomId");
        String userMessage = (String) data.get("message");

        // 🔥 AI 호출
        String aiAnswer = chattingService.askGemini(userMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("senderType", "AI");
        response.put("senderName", "AI상담사");
        response.put("message", aiAnswer);

        String json = mapper.writeValueAsString(response);

        // 🔥 AI 응답 전송
        session.sendMessage(new TextMessage(json));
    }

    private void broadcast(String roomId, TextMessage message) {
        rooms.get(roomId).forEach(ws -> {
            try {
                ws.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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