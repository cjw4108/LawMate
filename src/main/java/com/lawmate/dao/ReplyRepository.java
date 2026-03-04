package com.lawmate.dao;

import com.lawmate.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

    // 기존에 있던 코드들
    List<ReplyEntity> findByQuestionIdOrderByCreatedAtAsc(Long questionId);

    int countByQuestionId(Long questionId);

    @Transactional
        // ✅ 추가
    void deleteByQuestionId(Long questionId);
}