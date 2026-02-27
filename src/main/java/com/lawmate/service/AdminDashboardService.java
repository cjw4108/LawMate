package com.lawmate.service;

import com.lawmate.dao.QuestionRepository;
import com.lawmate.dto.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDashboardService {

    private final QuestionRepository questionRepository;

    // 미답변 질문 목록
    @Transactional(readOnly = true)
    public List<Question> getUnansweredQuestions() {
        return questionRepository.findByAnsweredOrderByCreatedAtDesc(0);
    }

    // 신고된 질문 목록
    @Transactional(readOnly = true)
    public List<Question> getReportedQuestions() {
        return questionRepository.findByReportCountGreaterThanOrderByReportCountDesc(0);
    }
}