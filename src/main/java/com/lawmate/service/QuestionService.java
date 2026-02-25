package com.lawmate.service;

import com.lawmate.dao.QuestionRepository;
import com.lawmate.dao.QuestionReportRepository;
import com.lawmate.dao.ReplyRepository;
import com.lawmate.dto.Question;
import com.lawmate.dto.QuestionListDTO;
import com.lawmate.dto.QuestionReport;
import com.lawmate.entity.ReplyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionReportRepository reportRepository;
    private final ReplyRepository replyRepository;

    // =====================================================
    // 질문 저장
    // =====================================================
    public void save(Question question) {
        questionRepository.save(question);
    }

    // =====================================================
    // 🔥 최신순 (JOIN 1번)
    // =====================================================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByCreatedAtDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByLatest()
        );
    }

    // =====================================================
    // 🔥 답변 많은 순
    // =====================================================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByReplyCountDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByReply()
        );
    }

    // =====================================================
    // 🔥 좋아요 많은 순
    // =====================================================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByLikesDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByLikes()
        );
    }

    // =====================================================
    // 제목 검색 (검색은 기존 엔티티 반환)
    // =====================================================
    @Transactional(readOnly = true)
    public List<Question> search(String keyword) {
        return questionRepository
                .findByTitleContainingOrderByCreatedAtDesc(keyword);
    }

    @Transactional(readOnly = true)
    public List<Question> findMyFavorites(String userId) {
        return questionRepository.findMyFavorites(userId);
    }

    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // =====================================================
    // 🔥 통합 리스트
    // =====================================================
    @Transactional(readOnly = true)
    public List<?> getList(String keyword, String sort, String userId) {

        if (keyword != null && !keyword.isEmpty()) {
            return search(keyword);
        }

        switch (sort) {
            case "replies":
                return findAllByOrderByReplyCountDesc();
            case "likes":
                return findAllByOrderByLikesDesc();
            case "favorite":
                if (userId == null) return Collections.emptyList();
                return findMyFavorites(userId);
            case "latest":
            default:
                return findAllByOrderByCreatedAtDesc();
        }
    }

    // =====================================================
    // 찜 관련
    // =====================================================
    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

    @Transactional(readOnly = true)
    public int getFavoriteCount(Long id) {
        return questionRepository.countFavoriteByQuestion(id);
    }

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

    // =====================================================
    // 신고 관련
    // =====================================================
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

    // =====================================================
    // 답변 관련
    // =====================================================
    @Transactional
    public void registerReply(Long id, String content, String userId) {

        questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        ReplyEntity entity = new ReplyEntity();
        entity.setQuestionId(id);
        entity.setUserId(userId);
        entity.setContent(content);
        entity.setCreatedAt(LocalDateTime.now());

        replyRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<ReplyEntity> getReplies(Long questionId) {
        return replyRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    @Transactional(readOnly = true)
    public int getReplyCount(Long questionId) {
        return replyRepository.countByQuestionId(questionId);
    }

    // =====================================================
    // 🔥 Object[] → DTO 매핑 (안전 버전)
    // =====================================================
    private List<QuestionListDTO> mapToDTO(List<Object[]> rows) {

        return rows.stream().map(row -> new QuestionListDTO(
                ((Number) row[0]).longValue(),          // ID
                (String) row[1],                       // USER_ID
                (String) row[2],                       // TITLE
                (String) row[3],                       // CONTENT
                row[4] == null ? 0 : ((Number) row[4]).intValue(),  // ANSWERED
                row[5] == null ? 0 : ((Number) row[5]).intValue(),  // REPORT_COUNT
                ((Timestamp) row[6]).toLocalDateTime(),             // CREATED_AT
                ((Number) row[7]).intValue(),          // replyCount
                ((Number) row[8]).intValue()           // favoriteCount
        )).toList();
    }
}