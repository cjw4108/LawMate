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

    // 1. 질문 저장 (이 메서드가 없어서 에러가 났던 것입니다)
    public void save(Question question) {
        questionRepository.save(question);
    }

    // 2. 최신순 조회
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByCreatedAtDesc() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    // 3. 답변 많은 순 조회
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByAnsweredDesc() {
        return questionRepository.findAllByOrderByAnsweredDesc();
    }

    // 4. 공감(좋아요) 많은 순 조회
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByLikesDesc() {
        return questionRepository.findAllByOrderByLikesDesc();
    }

    // 5. 검색 조회
    @Transactional(readOnly = true)
    public List<Question> search(String keyword) {
        return questionRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
    }

    // 6. 내가 찜한 게시글 조회
    @Transactional(readOnly = true)
    public List<Question> findMyFavorites(String userId) {
        return questionRepository.findMyFavorites(userId);
    }

    // 7. 상세 조회
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // 8. 찜 상태 확인
    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

    // 9. 신고 처리 (QUESTIONS 카운트 증가 + QUESTION_REPORTS 사유 저장)
    public void report(Long qnaId, String reason, String userId) {
        Question q = questionRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 카운트 증가
        q.setReportCount((q.getReportCount() == null ? 0 : q.getReportCount()) + 1);

        // 사유 저장
        QuestionReport report = QuestionReport.builder()
                .qnaId(qnaId)
                .userId(userId)
                .reason(reason)
                .build();
        reportRepository.save(report);
    }

    // 10. 관리자용 신고 목록 조회
    @Transactional(readOnly = true)
    public List<Question> findReportedQuestions() {
        return questionRepository.findByReportCountGreaterThanOrderByReportCountDesc(0);
    }

    // 11. 찜하기 토글 (로직은 프로젝트 상황에 맞춰 구현 필요)
    public boolean toggleFavorite(Long id, String userId) {
        // 여기에 찜하기 테이블 insert/delete 로직을 추가하세요.
        return true;
    }
}