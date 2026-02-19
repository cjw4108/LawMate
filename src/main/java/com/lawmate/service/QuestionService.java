package com.lawmate.service;

import com.lawmate.dao.QuestionRepository;
import com.lawmate.dao.QuestionReportRepository;
import com.lawmate.dto.Question;
import com.lawmate.dto.QuestionReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionReportRepository reportRepository;

    // 질문 저장
    public void save(Question question) {
        questionRepository.save(question);
    }

    // 최신순 조회
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByCreatedAtDesc() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    // 답변 많은 순 조회
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByAnsweredDesc() {
        return questionRepository.findAllByOrderByAnsweredDesc();
    }

    // 공감(좋아요) 많은 순 조회 (Native Query)
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByLikesDesc() {
        return questionRepository.findAllByOrderByLikesDesc();
    }

    // 제목 검색
    @Transactional(readOnly = true)
    public List<Question> search(String keyword) {
        return questionRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
    }

    // 내가 찜한 게시글 목록
    @Transactional(readOnly = true)
    public List<Question> findMyFavorites(String userId) {
        return questionRepository.findMyFavorites(userId);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // 찜 여부 확인
    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

    /**
     * 신고 처리: 질문 테이블의 카운트를 올리고 상세 사유를 저장합니다.
     */
    @Transactional
    public void report(Long qnaId, String reason, String userId) {
        Question q = questionRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 1. QUESTIONS 테이블의 REPORT_COUNT 증가
        int currentCount = (q.getReportCount() == null) ? 0 : q.getReportCount();
        q.setReportCount(currentCount + 1);

        // 중요: 즉시 DB에 반영(Flush)하여 관리자 페이지 조회 시 데이터가 나오게 함
        questionRepository.saveAndFlush(q);

        // 2. QUESTION_REPORTS 테이블에 상세 사유 저장
        QuestionReport report = QuestionReport.builder()
                .qnaId(qnaId)
                .userId(userId)
                .reason(reason)
                .build();
        reportRepository.save(report);
    }

    /**
     * 관리자용 신고 목록 조회: 신고 횟수가 1회 이상인 게시글만 가져옵니다.
     */
    @Transactional(readOnly = true)
    public List<Question> findReportedQuestions() {
        return questionRepository.findByReportCountGreaterThanOrderByReportCountDesc(0);
    }

    // 찜하기 토글 (필요 시 구현)
    public boolean toggleFavorite(Long id, String userId) {
        return true;
    }
}