package com.lawmate.service;

import com.lawmate.dao.ReplyRepository;
import com.lawmate.dto.Reply;
import com.lawmate.entity.ReplyEntity;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public void saveReply(Reply reply) {
        ReplyEntity entity = ReplyEntity.builder()
                .questionId(reply.getQuestionId())
                .content(reply.getContent())
                .userId(reply.getUserId())
                .build();
        replyRepository.save(entity);
    }

    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

    public List<Reply> getReplies(Long qnaId) {
        return replyRepository.findByQuestionIdOrderByCreatedAtAsc(qnaId)
                .stream()
                .map(entity -> Reply.builder()
                        .id(entity.getId())
                        .questionId(entity.getQuestionId())
                        .content(entity.getContent())
                        .userId(entity.getUserId())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // ✅ 추가
    public void deleteByQnaId(Long qnaId) {
        replyRepository.deleteByQuestionId(qnaId);
    }
}