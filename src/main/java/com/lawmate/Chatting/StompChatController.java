package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final RedisPublisher redisPublisher;
    private final A03_ChattingService chattingService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        try {
            chattingService.saveMessage(
                    message.getRoomId(), message.getSenderId(),
                    message.getSenderType(), message.getMessage()
            );
            redisPublisher.publish(message);

            // ✅ AI 방일 때만 AI 응답
            if ("USER".equals(message.getSenderType())
                    && chattingService.isAiRoom(message.getRoomId())) {

                CompletableFuture.runAsync(() -> {
                    try {
                        String aiAnswer;
                        if (message.getFilePath() != null && !message.getFilePath().trim().isEmpty()) {
                            aiAnswer = chattingService.requestAiAnalysisByPath(message.getFilePath());
                        } else {
                            aiAnswer = chattingService.askGemini(message.getMessage(), null);
                        }

                        ChatMessage aiMsg = new ChatMessage();
                        aiMsg.setRoomId(message.getRoomId());
                        aiMsg.setSenderId("GEMINI_AI");
                        aiMsg.setSenderName("법률 AI");
                        aiMsg.setSenderType("AI");
                        aiMsg.setMessage(aiAnswer);

                        chattingService.saveMessage(
                                aiMsg.getRoomId(), aiMsg.getSenderId(),
                                aiMsg.getSenderType(), aiMsg.getMessage()
                        );
                        redisPublisher.publish(aiMsg);

                    } catch (Exception e) {
                        System.err.println("AI 응답 오류: " + e.getMessage());
                    }
                });
            }

        } catch (Exception e) {
            System.err.println("메시지 처리 오류: " + e.getMessage());
        }
    }


    private boolean isAiRoom(String roomId) {
        return roomId.contains("AI_CHAT");
    }
}