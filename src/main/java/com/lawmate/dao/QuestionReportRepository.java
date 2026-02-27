package com.lawmate.dao;

import com.lawmate.dto.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionReportRepository extends JpaRepository<QuestionReport, Long> {

    @Query(value = """
        SELECT REASON, USER_ID, CREATED_AT
        FROM QUESTION_REPORTS
        WHERE QNA_ID = :qnaId
        ORDER BY CREATED_AT DESC
        """, nativeQuery = true)
    List<Object[]> findReasonsByQnaId(@Param("qnaId") Long qnaId);
}