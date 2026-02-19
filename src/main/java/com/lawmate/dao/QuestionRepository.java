package com.lawmate.dao;

import com.lawmate.dto.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 최신순
    List<Question> findAllByOrderByCreatedAtDesc();

    // 답변 많은 순
    List<Question> findAllByOrderByAnsweredDesc();

    // 제목 검색
    List<Question> findByTitleContainingOrderByCreatedAtDesc(String keyword);

    // 신고된 질문 목록 (관리자용)
    List<Question> findByReportCountGreaterThanOrderByReportCountDesc(int count);

    // 공감(좋아요) 많은 순 (Native Query)
    @Query(value = "SELECT q.* FROM QUESTIONS q LEFT JOIN QUESTION_LIKES l ON q.ID = l.QNA_ID " +
            "GROUP BY q.ID, q.USER_ID, q.TITLE, q.CONTENT, q.ANSWERED, q.ADOPTED_ANSWER, q.REPORT_COUNT, q.CREATED_AT " +
            "ORDER BY COUNT(l.ID) DESC", nativeQuery = true)
    List<Question> findAllByOrderByLikesDesc();

    // 내가 찜한 게시글 목록 (Native Query)
    @Query(value = "SELECT q.* FROM QUESTIONS q JOIN QUESTION_LIKES l ON q.ID = l.QNA_ID WHERE l.USER_ID = :userId", nativeQuery = true)
    List<Question> findMyFavorites(@Param("userId") String userId);

    // 특정 유저가 특정 게시글을 찜했는지 확인 (상세페이지용)
    @Query(value = "SELECT COUNT(*) FROM QUESTION_LIKES WHERE QNA_ID = :qnaId AND USER_ID = :userId", nativeQuery = true)
    int countFavorite(@Param("qnaId") Long qnaId, @Param("userId") String userId);
}