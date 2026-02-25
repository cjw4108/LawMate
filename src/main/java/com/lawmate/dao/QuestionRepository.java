package com.lawmate.dao;

import com.lawmate.dto.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // =====================================================
    // 🔥 최신순 + 답변수 + 좋아요수 (DTO용)
    // =====================================================
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.cnt, 0) AS replyCount,
            NVL(l.cnt, 0) AS favoriteCount
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_LIKES
            GROUP BY QNA_ID
        ) l ON q.ID = l.QNA_ID
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByLatest();


    // =====================================================
    // 🔥 답변 많은 순
    // =====================================================
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.cnt, 0),
            NVL(l.cnt, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_LIKES
            GROUP BY QNA_ID
        ) l ON q.ID = l.QNA_ID
        ORDER BY NVL(r.cnt, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByReply();


    // =====================================================
    // 🔥 좋아요 많은 순
    // =====================================================
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.cnt, 0),
            NVL(l.cnt, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS cnt
            FROM QUESTION_LIKES
            GROUP BY QNA_ID
        ) l ON q.ID = l.QNA_ID
        ORDER BY NVL(l.cnt, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByLikes();


    // =========================
    // 나머지는 그대로 유지
    // =========================

    List<Question> findByTitleContainingOrderByCreatedAtDesc(String keyword);

    List<Question> findByReportCountGreaterThanOrderByReportCountDesc(int count);

    @Query(value = """
        SELECT q.* 
        FROM QUESTIONS q 
        JOIN QUESTION_LIKES l 
          ON q.ID = l.QNA_ID 
        WHERE l.USER_ID = :userId
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Question> findMyFavorites(@Param("userId") String userId);

    @Query(value = """
        SELECT COUNT(*) 
        FROM QUESTION_LIKES 
        WHERE QNA_ID = :qnaId 
          AND USER_ID = :userId
        """, nativeQuery = true)
    int countFavorite(@Param("qnaId") Long qnaId,
                      @Param("userId") String userId);

    @Query(value = """
        SELECT COUNT(*) 
        FROM QUESTION_LIKES 
        WHERE QNA_ID = :qnaId
        """, nativeQuery = true)
    int countFavoriteByQuestion(@Param("qnaId") Long qnaId);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO QUESTION_LIKES 
        (LIKE_ID, QNA_ID, USER_ID) 
        VALUES (SEQ_LIKES_ID.NEXTVAL, :qnaId, :userId)
        """, nativeQuery = true)
    void insertFavorite(@Param("qnaId") Long qnaId,
                        @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM QUESTION_LIKES 
        WHERE QNA_ID = :qnaId 
          AND USER_ID = :userId
        """, nativeQuery = true)
    void deleteFavorite(@Param("qnaId") Long qnaId,
                        @Param("userId") String userId);
}