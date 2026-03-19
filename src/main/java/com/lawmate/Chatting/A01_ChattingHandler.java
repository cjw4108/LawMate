package com.lawmate.Chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;

@Component("chatHandler")
public class A01_ChattingHandler extends TextWebSocketHandler {

    @Autowired
    private A03_ChattingService chattingService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> data = mapper.readValue(payload, Map.class);

        String roomId = (String) data.get("roomId");
        String userMessage = (String) data.get("message");
        String filePath = (String) data.get("filePath");

        // 🚨 [긴급 진단 로그] - 이 로그가 인텔리제이에 찍히는지 보세요!
        System.err.println("========================================");
        System.err.println("📩 [수신 데이터 확인]");
        System.err.println("   - 메시지: " + userMessage);
        System.err.println("   - 파일경로: " + filePath);
        System.err.println("========================================");

        String aiAnswer;

        // 1. 파일이 있는 경우 -> 파이썬 서버 호출
        if (filePath != null && !filePath.trim().isEmpty()) {
            System.err.println("🚀 [동작] 파일 분석 모드로 진입합니다.");
            aiAnswer = chattingService.requestAiAnalysisByPath(filePath);
        }
        // 2. 파일이 없는 경우 -> 일반 Gemini 호출
        else {
            System.err.println("💬 [동작] 일반 채팅 모드로 진입합니다.");
            aiAnswer = chattingService.askGemini(userMessage, null);
        }

        // 결과 전송
        String jsonResponse = mapper.writeValueAsString(Map.of(
                "roomId", roomId,
                "senderType", "AI",
                "senderName", "AI상담사",
                "message", aiAnswer
        ));
        session.sendMessage(new TextMessage(jsonResponse));

        // DB 저장
        chattingService.saveMessageAsync(roomId, "GEMINI_AI", "AI", aiAnswer);
    }
}