package com.lawmate.dao;

import com.lawmate.entity.ReplyEntity;  // ← 이거 필수!
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByQuestionIdOrderByCreatedAtAsc(Long questionId);

    int countByQuestionId(Long questionId);
}