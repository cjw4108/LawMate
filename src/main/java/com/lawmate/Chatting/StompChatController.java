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

    @MessageMapping("/chat/message") // JS에서 .send("/pub/chat/message", ...) 보낼 때 호출
    public void handleMessage(ChatMessage message) {

        // 1. DB 저장 (비동기)
        CompletableFuture.runAsync(() -> {
            chattingService.saveMessage(
                    message.getRoomId(),
                    message.getSenderId(),
                    message.getSenderType(),
                    message.getMessage()
            );
        });

        // 2. AI 상담인 경우 (기존 핸들러 로직 이식)
        if ("AI".equals(message.getSenderType()) || "GEMINI_AI".equals(message.getSenderId())) {
            // AI 로직 수행... (필요시 추가)
        }

        // 3. 🚀 핵심: Redis로 메시지 발행 (이게 있어야 실시간으로 보임!)
        // RedisPublisher가 메시지를 던지면 RedisSubscriber가 받아서 화면에 뿌려줍니다.
        redisPublisher.publish(message);
    }
}