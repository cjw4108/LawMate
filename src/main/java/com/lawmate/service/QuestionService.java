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

    // ============================
    // 질문 저장
    // ============================
    public void save(Question question) {
        questionRepository.save(question);
    }

    // ============================
    // 단건 조회
    // ============================
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // ============================
    // 일반 게시판 기능
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByCreatedAtDesc() {
        return mapToDTO(questionRepository.findAllWithCountsOrderByLatest());
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByReplyCountDesc() {
        return mapToDTO(questionRepository.findAllWithCountsOrderByReply());
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByLikesDesc() {
        return mapToDTO(questionRepository.findAllWithCountsOrderByLikes());
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> search(String keyword) {
        return mapToDTO(questionRepository.searchWithCounts(keyword));
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> findMyFavorites(String userId) {
        if (userId == null) return Collections.emptyList();
        return mapToDTO(questionRepository.findMyFavoritesWithCounts(userId));
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> getList(String keyword, String sort, String userId) {
        if (keyword != null && !keyword.isEmpty()) {
            return search(keyword);
        }
        return switch (sort) {
            case "replies" -> findAllByOrderByReplyCountDesc();
            case "likes" -> findAllByOrderByLikesDesc();
            case "favorite" -> findMyFavorites(userId);
            default -> findAllByOrderByCreatedAtDesc();
        };
    }

    // ============================
    // 찜(Favorite) 관련
    // ============================
    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

    @Transactional
    public String toggleFavorite(Long id, String userId) {
        if (isFavorite(id, userId)) {
            questionRepository.deleteFavorite(id, userId);
            questionRepository.decrementFavoriteCount(id);
            return "removed";
        } else {
            questionRepository.insertFavorite(id, userId);
            questionRepository.incrementFavoriteCount(id);
            return "added";
        }
    }

    // ============================
    // 신고 처리
    // ============================
    @Transactional
    public void report(Long qnaId, String reason, String userId) {
        Question q = questionRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        int currentCount = q.getReportCount() == null ? 0 : q.getReportCount();
        q.setReportCount(currentCount + 1);
        questionRepository.save(q);

        QuestionReport report = QuestionReport.builder()
                .qnaId(qnaId)
                .userId(userId)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();
        reportRepository.save(report);
    }

    // ============================
    // 답변 관련
    // ============================
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

        // 답변 등록 시 answered 컬럼 업데이트
        Question q = questionRepository.findById(id).get();
        q.setAnswered(1);
        questionRepository.save(q);
    }

    // ============================
    // 관리자 기능
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> getAdminQuestionList(String filter, String sort) {
        List<Object[]> rows;
        if ("deleted".equals(filter)) {
            rows = questionRepository.findAllDeletedWithCounts();
        } else if ("report".equals(sort)) {
            rows = questionRepository.findAllReportedWithCounts();
        } else if ("unanswered".equals(filter)) {
            rows = questionRepository.findAllUnansweredWithCounts();
        } else {
            rows = questionRepository.findAllWithCountsOrderByLatest();
        }
        return mapToDTO(rows);
    }

    @Transactional(readOnly = true)
    public int getUnansweredCount() {
        return questionRepository.countUnanswered();
    }

    @Transactional(readOnly = true)
    public int getReportedCount() {
        return questionRepository.countReported();
    }

    public void softDelete(Long id) {
        questionRepository.softDelete(id);
    }

    public void restore(Long id) {
        questionRepository.restore(id);
    }

    // ============================
    // DTO 매핑
    // ============================
    private List<QuestionListDTO> mapToDTO(List<Object[]> rows) {
        return rows.stream().map(this::basicMapping).toList();
    }

    private QuestionListDTO basicMapping(Object[] row) {
        Long id = row[0] == null ? 0L : ((Number) row[0]).longValue();
        String userId = row[1] == null ? "" : row[1].toString();
        String title = row[2] == null ? "" : row[2].toString();
        String content = row[3] == null ? "" : row[3].toString();
        Integer answered = row[4] == null ? 0 : ((Number) row[4]).intValue();
        Integer reportCount = row[5] == null ? 0 : ((Number) row[5]).intValue();

        LocalDateTime createdAt;
        if (row[6] instanceof Timestamp ts) {
            createdAt = ts.toLocalDateTime();
        } else if (row[6] instanceof LocalDateTime ldt) {
            createdAt = ldt;
        } else {
            createdAt = LocalDateTime.now();
        }

        int replyCount = row[7] == null ? 0 : ((Number) row[7]).intValue();
        int favoriteCount = row[8] == null ? 0 : ((Number) row[8]).intValue();

        return new QuestionListDTO(
                id, userId, title, content, answered, reportCount,
                createdAt, replyCount, favoriteCount
        );
    }

    // ============================
    // FAVORITE COUNT 조회
    // ============================
    @Transactional(readOnly = true)
    public int getFavoriteCount(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) return 0;
        return question.getFavoriteCount() == null ? 0 : question.getFavoriteCount();
    }

    // ============================
    // 댓글 목록 조회
    // ============================
    @Transactional(readOnly = true)
    public List<ReplyEntity> getReplies(Long questionId) {
        return replyRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    // ============================
    // 댓글 개수 조회
    // ============================
    @Transactional(readOnly = true)
    public int getReplyCount(Long questionId) {
        return replyRepository.countByQuestionId(questionId);
    }
}