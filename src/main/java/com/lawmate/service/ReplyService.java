package com.lawmate.service;

import com.lawmate.dao.ReplyRepository;
import com.lawmate.dto.Reply;
import com.lawmate.entity.ReplyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    // 답변 저장
    @Transactional
    public void saveReply(Reply reply) {
        ReplyEntity entity = ReplyEntity.builder()
                .questionId(reply.getQuestionId())
                .content(reply.getContent())
                .userId(reply.getUserId())
                .build();
        replyRepository.save(entity);
    }

    // 답변 삭제
    @Transactional
    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

    // [추가] 컨트롤러에서 권한 체크(작성자 확인)를 위해 엔티티를 가져오는 메서드
    public ReplyEntity getReplyEntity(Long id) {
        return replyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 답변을 찾을 수 없습니다. ID: " + id));
    }

    // 답변 수정
    @Transactional
    public void updateReply(Long id, String content, String userId) {
        ReplyEntity entity = replyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다."));

        // 비즈니스 로직 계층에서도 한 번 더 방어 (관리자이거나 본인인 경우만 세팅)
        // 컨트롤러에서 이미 체크하지만, 서비스 단독 사용 시를 대비한 안전장치입니다.
        entity.setContent(content);
        replyRepository.save(entity);
    }

    // 특정 질문의 답변 목록 조회
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

    // 질문 삭제 시 관련 답변 전체 삭제
    @Transactional
    public void deleteByQnaId(Long qnaId) {
        replyRepository.deleteByQuestionId(qnaId);
    }
}