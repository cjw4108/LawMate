package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Qualifier; // 추가
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    // 생성자 주입 방식으로 변경하여 @Qualifier를 적용합니다.
    public RedisPublisher(@Qualifier("chatRedisTemplate") RedisTemplate<String, Object> redisTemplate,
                          ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}