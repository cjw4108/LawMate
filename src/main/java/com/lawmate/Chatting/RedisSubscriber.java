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

    public void onMessage(Message message, byte[] pattern) {
        try {
            // 1. 메시지 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // 2. 브라우저로 전송 (이 주소가 중요!)
            // JSP가 /sub/chat/room/b685... 주소를 구독하고 있으므로 똑같이 맞춰줍니다.
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            log.error("Exception {}", e.getStackTrace());
        }
    }
}