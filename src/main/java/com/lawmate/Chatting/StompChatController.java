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
        // 사용자 메시지 저장
        chattingService.saveMessage(message.getRoomId(), message.getSenderId(), message.getSenderType(), message.getMessage());
        redisPublisher.publish(message);

        // AI 로직
        if ("USER".equals(message.getSenderType())) {
            String aiAnswer = chattingService.askGemini(message.getMessage());

            ChatMessage aiMsg = new ChatMessage();
            aiMsg.setRoomId(message.getRoomId());
            aiMsg.setSenderId("GEMINI_AI");
            aiMsg.setSenderName("법률 AI");
            aiMsg.setSenderType("AI");
            aiMsg.setMessage(aiAnswer);

            chattingService.saveMessage(aiMsg.getRoomId(), aiMsg.getSenderId(), aiMsg.getSenderType(), aiMsg.getMessage());
            redisPublisher.publish(aiMsg);
        }
    }
}