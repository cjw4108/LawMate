package com.lawmate.Chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawmate.dto.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate; // 변경됨
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisSubscriber.class);

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate; // 타입 변경

    @Autowired
    public RedisSubscriber(
            @Qualifier("chatRedisTemplate") RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            SimpMessagingTemplate messagingTemplate) { // 타입 변경
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 메시지 본문(body)을 읽어옴
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // JSON 문자열을 ChatMessage 객체로 역직렬화
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // 웹소켓을 구독 중인 클라이언트들에게 메시지 전파
            // 프론트엔드(JS)의 subscribe 경로와 일치해야 함: /sub/chat/room/{roomId}
            // 이 경로로 쏘고 있는지 확인
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

            log.info("[Redis 전파 성공] 방ID: {}, 보낸이: {}", chatMessage.getRoomId(), chatMessage.getSenderId());
        } catch (Exception e) {
            log.error("Redis Subscriber 에러 발생: {}", e.getMessage());
        }
    }
}