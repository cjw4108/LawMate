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

    // ==============================
    // 관리자/대시보드
    // ==============================
    List<Question> findByAnsweredOrderByCreatedAtDesc(int answered);
    List<Question> findByReportCountGreaterThanOrderByReportCountDesc(int reportCount);

    // ==============================
    // 최신순 조회
    // ==============================
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt, 0),
            NVL(q.FAVORITE_COUNT, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.DELETED = 0
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByLatest();

    // 댓글 많은 순
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt, 0),
            NVL(q.FAVORITE_COUNT, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.DELETED = 0
        ORDER BY NVL(r.reply_cnt, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByReply();

    // 좋아요 많은 순
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt, 0),
            NVL(q.FAVORITE_COUNT, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.DELETED = 0
        ORDER BY NVL(q.FAVORITE_COUNT, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByLikes();

    // 검색
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt, 0),
            NVL(q.FAVORITE_COUNT, 0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.DELETED = 0
          AND q.TITLE LIKE '%' || :keyword || '%'
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> searchWithCounts(@Param("keyword") String keyword);

    // ==============================
    // 찜(Favorite) 관련
    // ==============================
    @Query(value = """
        SELECT q.*
        FROM QUESTIONS q
        JOIN QUESTION_LIKES l ON q.ID = l.QNA_ID
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE l.USER_ID = :userId
          AND q.DELETED = 0
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findMyFavoritesWithCounts(@Param("userId") String userId);

    @Query(value = """
        SELECT COUNT(*)
        FROM QUESTION_LIKES
        WHERE QNA_ID = :qnaId
          AND USER_ID = :userId
        """, nativeQuery = true)
    int countFavorite(@Param("qnaId") Long qnaId, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO QUESTION_LIKES (LIKE_ID, QNA_ID, USER_ID)
        VALUES (SEQ_LIKES_ID.NEXTVAL, :qnaId, :userId)
        """, nativeQuery = true)
    void insertFavorite(@Param("qnaId") Long qnaId, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM QUESTION_LIKES
        WHERE QNA_ID = :qnaId
          AND USER_ID = :userId
        """, nativeQuery = true)
    void deleteFavorite(@Param("qnaId") Long qnaId, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE QUESTIONS
        SET FAVORITE_COUNT = FAVORITE_COUNT + 1
        WHERE ID = :qnaId
        """, nativeQuery = true)
    void incrementFavoriteCount(@Param("qnaId") Long qnaId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE QUESTIONS
        SET FAVORITE_COUNT = FAVORITE_COUNT - 1
        WHERE ID = :qnaId
        """, nativeQuery = true)
    void decrementFavoriteCount(@Param("qnaId") Long qnaId);

    // ==============================
    // 삭제/신고/미답변
    // ==============================
    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt,0),
            NVL(q.FAVORITE_COUNT,0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.DELETED = 1
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllDeletedWithCounts();

    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt,0),
            NVL(q.FAVORITE_COUNT,0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE q.REPORT_COUNT > 0
          AND q.DELETED = 0
        ORDER BY q.REPORT_COUNT DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllReportedWithCounts();

    @Query(value = """
        SELECT 
            q.ID,
            q.USER_ID,
            q.TITLE,
            q.CONTENT,
            q.ANSWERED,
            q.REPORT_COUNT,
            q.CREATED_AT,
            NVL(r.reply_cnt,0),
            NVL(q.FAVORITE_COUNT,0)
        FROM QUESTIONS q
        LEFT JOIN (
            SELECT QNA_ID, COUNT(*) AS reply_cnt
            FROM QUESTION_REPLIES
            GROUP BY QNA_ID
        ) r ON q.ID = r.QNA_ID
        WHERE (q.ANSWERED = 0 OR q.ANSWERED IS NULL)
          AND q.DELETED = 0
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllUnansweredWithCounts();

    @Query("""
        SELECT COUNT(q)
        FROM Question q
        WHERE (q.answered = 0 OR q.answered IS NULL)
          AND q.deleted = 0
        """)
    int countUnanswered();

    @Query("""
        SELECT COUNT(q)
        FROM Question q
        WHERE q.reportCount > 0
          AND q.deleted = 0
        """)
    int countReported();

    @Modifying
    @Transactional
    @Query("""
        UPDATE Question q
        SET q.deleted = 1
        WHERE q.id = :id
        """)
    void softDelete(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Question q
        SET q.deleted = 0
        WHERE q.id = :id
        """)
    void restore(@Param("id") Long id);
}