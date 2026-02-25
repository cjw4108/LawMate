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
    // ✅ 최신순 + 답변수 + 좋아요수 한번에 조회 (🔥 핵심 최적화)
    // =====================================================
    @Query(value = """
        SELECT q.*,
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
    // ✅ 답변 많은 순 정렬 (최적화 버전)
    // =====================================================
    @Query(value = """
        SELECT q.*,
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
        ORDER BY NVL(r.cnt, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByReply();


    // =====================================================
    // ✅ 좋아요 많은 순 정렬 (최적화 버전)
    // =====================================================
    @Query(value = """
        SELECT q.*,
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
        ORDER BY NVL(l.cnt, 0) DESC, q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findAllWithCountsOrderByLikes();


    // =====================================================
    // 제목 검색
    // =====================================================
    List<Question> findByTitleContainingOrderByCreatedAtDesc(String keyword);


    // =====================================================
    // 신고된 질문 목록 (관리자용)
    // =====================================================
    List<Question> findByReportCountGreaterThanOrderByReportCountDesc(int count);


    // =====================================================
    // 내가 찜한 게시글 목록
    // =====================================================
    @Query(value = """
        SELECT q.* 
        FROM QUESTIONS q 
        JOIN QUESTION_LIKES l 
          ON q.ID = l.QNA_ID 
        WHERE l.USER_ID = :userId
        ORDER BY q.CREATED_AT DESC
        """, nativeQuery = true)
    List<Question> findMyFavorites(@Param("userId") String userId);


    // =====================================================
    // 특정 게시글을 해당 유저가 찜했는지 확인
    // =====================================================
    @Query(value = """
        SELECT COUNT(*) 
        FROM QUESTION_LIKES 
        WHERE QNA_ID = :qnaId 
          AND USER_ID = :userId
        """, nativeQuery = true)
    int countFavorite(@Param("qnaId") Long qnaId,
                      @Param("userId") String userId);


    // =====================================================
    // 게시글별 전체 찜 개수 조회
    // =====================================================
    @Query(value = """
        SELECT COUNT(*) 
        FROM QUESTION_LIKES 
        WHERE QNA_ID = :qnaId
        """, nativeQuery = true)
    int countFavoriteByQuestion(@Param("qnaId") Long qnaId);


    // =====================================================
    // 찜 추가
    // =====================================================
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO QUESTION_LIKES 
        (LIKE_ID, QNA_ID, USER_ID) 
        VALUES (SEQ_LIKES_ID.NEXTVAL, :qnaId, :userId)
        """, nativeQuery = true)
    void insertFavorite(@Param("qnaId") Long qnaId,
                        @Param("userId") String userId);


    // =====================================================
    // 찜 삭제
    // =====================================================
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