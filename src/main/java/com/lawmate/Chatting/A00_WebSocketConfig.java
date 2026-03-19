package com.lawmate.Chatting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket              // 🌟 일반 웹소켓 핸들러(사진 분석) 활성화
@EnableWebSocketMessageBroker // 🌟 STOMP(실시간 알림 및 RedisSubscriber 에러 해결) 활성화
public class A00_WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Autowired
    private A01_ChattingHandler chatHandler;

    // 1. 일반 웹소켓 핸들러 등록 (A01_ChattingHandler와 연결)
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 프론트의 ws://localhost:8080/chatSocket 접속을 처리합니다.
        registry.addHandler(chatHandler, "/chatSocket")
                .setAllowedOriginPatterns("*");
    }

    // 2. STOMP 메시지 브로커 설정 (RedisSubscriber가 필요한 빈 생성)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    // 3. STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}