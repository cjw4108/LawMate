package com.lawmate.service;

import com.lawmate.dao.QuestionRepository;
import com.lawmate.dao.QuestionReportRepository;
import com.lawmate.dto.Question;
import com.lawmate.dto.QuestionReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionReportRepository reportRepository;

    // =========================
    // 질문 저장
    // =========================
    public void save(Question question) {
        questionRepository.save(question);
    }

    // =========================
    // 목록 조회
    // =========================
    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByCreatedAtDesc() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByAnsweredDesc() {
        return questionRepository.findAllByOrderByAnsweredDesc();
    }

    @Transactional(readOnly = true)
    public List<Question> findAllByOrderByLikesDesc() {
        return questionRepository.findAllByOrderByLikesDesc();
    }

    @Transactional(readOnly = true)
    public List<Question> search(String keyword) {
        return questionRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
    }

    @Transactional(readOnly = true)
    public List<Question> findMyFavorites(String userId) {
        return questionRepository.findMyFavorites(userId);
    }

    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // =========================
    // 찜 관련
    // =========================

    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

    /**
     * ⭐ 찜 개수 조회 (AJAX 숫자 업데이트용)
     */
    @Transactional(readOnly = true)
    public int getFavoriteCount(Long id) {
        return questionRepository.countFavoriteByQuestion(id);
    }

    /**
     * 찜 토글
     */
    @Transactional
    public String toggleFavorite(Long id, String userId) {

        if (isFavorite(id, userId)) {
            questionRepository.deleteFavorite(id, userId);
            return "removed";
        } else {
            questionRepository.insertFavorite(id, userId);
            return "added";
        }
    }

    // =========================
    // 신고 관련
    // =========================
    @Transactional
    public void report(Long qnaId, String reason, String userId) {

        Question q = questionRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        int currentCount = (q.getReportCount() == null) ? 0 : q.getReportCount();
        q.setReportCount(currentCount + 1);

        questionRepository.saveAndFlush(q);

        QuestionReport report = QuestionReport.builder()
                .qnaId(qnaId)
                .userId(userId)
                .reason(reason)
                .build();

        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<Question> findReportedQuestions() {
        return questionRepository
                .findByReportCountGreaterThanOrderByReportCountDesc(0);
    }

    // =========================
    // 통합 리스트 (검색 + 정렬)
    // =========================
    @Transactional(readOnly = true)
    public List<Question> getList(String keyword, String sort, String userId) {

        if (keyword != null && !keyword.isEmpty()) {
            return questionRepository
                    .findByTitleContainingOrderByCreatedAtDesc(keyword);
        }

        switch (sort) {
            case "replies":
                return questionRepository.findAllByOrderByAnsweredDesc();
            case "likes":
                return questionRepository.findAllByOrderByLikesDesc();
            case "favorite":
                if (userId == null) return Collections.emptyList();
                return questionRepository.findMyFavorites(userId);
            case "latest":
            default:
                return questionRepository.findAllByOrderByCreatedAtDesc();
        }
    }

    // =========================
    // 답변 등록
    // =========================
    @Transactional
    public void registerReply(Long id, String content, String userId) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        int currentAnswered =
                (question.getAnswered() == null) ? 0 : question.getAnswered();

        question.setAnswered(currentAnswered + 1);

        questionRepository.save(question);

        // replyRepository 저장 로직이 있다면 여기에 추가
    }
}