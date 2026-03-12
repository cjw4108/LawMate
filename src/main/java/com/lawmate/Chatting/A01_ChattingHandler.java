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

            // [추가] 파일 정보 가져오기
            String fileName = (String) data.get("fileName");
            String filePath = (String) data.get("filePath");

            // 1️⃣ [공통 로직] DB 저장 (파일이 있다면 파일 경로도 같이 저장하는 로직이 Mapper에 있다면 수정 필요)
            CompletableFuture.runAsync(() -> {
                try {
                    chattingService.saveMessage(roomId, userId, senderType, userMessage);
                    System.out.println("✅ DB 저장 완료 (" + senderType + "): " + userMessage);
                } catch (Exception e) {
                    System.err.println("❌ 메시지 DB 저장 실패: " + e.getMessage());
                }
            });

            // 2️⃣ [분기 로직] AI 상담 vs 변호사 상담
            if ("AI".equals(chatWith)) {

                // 🚀 [핵심 추가] AI가 파일을 인식하게 만드는 로직
                String finalMessageForAI = userMessage;

                if (filePath != null && !filePath.isEmpty()) {
                    System.out.println("📂 파일 분석 요청 중... 경로: " + filePath);

                    // 1. 파이썬 서버 호출하여 파일 분석 내용 가져오기
                    String analysisResult = chattingService.requestAiAnalysisByPath(filePath);

                    // 2. Gemini에게 보낼 메시지 재구성 (분석 내용 + 사용자 질문)
                    finalMessageForAI = "[첨부파일 분석 내용]\n" + analysisResult +
                            "\n\n[사용자 질문]\n" + userMessage;
                }

                // 재구성된 메시지로 Gemini 호출
                String aiAnswer = chattingService.askGemini(finalMessageForAI);
                sendResponse(session, roomId, "AI", "AI상담사", aiAnswer);

                // AI 답변도 DB에 저장
                CompletableFuture.runAsync(() -> {
                    chattingService.saveMessage(roomId, "GEMINI_AI", "AI", aiAnswer);
                });

            } else {
                // ⚖️ 변호사 모드: 상대방에게 파일 정보까지 포함된 payload 그대로 전달
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