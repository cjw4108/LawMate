package com.lawmate.dao;

import com.lawmate.dto.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionReportRepository extends JpaRepository<QuestionReport, Long> {
    // 이제 외부 패키지(service)에서 접근이 가능합니다.
}