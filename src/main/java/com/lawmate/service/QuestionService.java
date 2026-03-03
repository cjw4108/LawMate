package com.lawmate.service;

import com.lawmate.dao.QuestionRepository;
import com.lawmate.dao.QuestionReportRepository;
import com.lawmate.dao.ReplyRepository;
import com.lawmate.dto.Question;
import com.lawmate.dto.QuestionListDTO;
import com.lawmate.dto.QuestionReport;
import com.lawmate.entity.ReplyEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionReportRepository reportRepository;
    private final ReplyRepository replyRepository;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionReportRepository reportRepository,
                           ReplyRepository replyRepository) {
        this.questionRepository = questionRepository;
        this.reportRepository = reportRepository;
        this.replyRepository = replyRepository;
    }

    // ============================
    // 1. 기본 CRUD 및 상세 조회
    // ============================
    public void save(Question question) {
        questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // ============================
    // 2. 관리자 통계 및 대시보드
    // ============================
    @Transactional(readOnly = true)
    public int getUnansweredCount() {
        return questionRepository.countUnanswered();
    }

    @Transactional(readOnly = true)
    public int getReportedCount() {
        return questionRepository.countReported();
    }

    // ============================
    // 3. 관리자 전용 리스트 관리 (삭제/복구/필터)
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> getAdminQuestionList(String filter, String sort) {
        List<Object[]> rows;

        if ("deleted".equals(filter)) {
            rows = questionRepository.findAllDeletedWithCounts();
        } else if ("report".equals(sort)) {
            rows = questionRepository.findAllReportedWithCounts();
        } else {
            rows = questionRepository.findAllWithCountsOrderByLatest();
        }
        return mapToDTO(rows);
    }

    // ⭐ 신고 사유 리스트 상세 조회 (Native Query 결과 리턴)
    @Transactional(readOnly = true)
    public List<Object[]> getReportReasonsByQnaId(Long qnaId) {
        // [사유, 신고자ID, 날짜] 순서의 리스트 반환
        return reportRepository.findReasonsByQnaId(qnaId);
    }

    public void softDelete(Long id) {
        questionRepository.softDelete(id);
    }

    public void restore(Long id) {
        questionRepository.restore(id);
    }

    // ============================
    // 4. 사용자 게시판 리스트 및 검색
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> getList(String keyword, String sort, String userId) {
        if (keyword != null && !keyword.isEmpty()) {
            return mapToDTO(questionRepository.searchWithCounts(keyword));
        }
        if ("likes".equals(sort)) {
            return mapToDTO(questionRepository.findAllWithCountsOrderByLikes());
        }
        if ("favorite".equals(sort)) {
            if (userId == null) return Collections.emptyList();
            return mapToDTO(questionRepository.findMyFavoritesWithCounts(userId));
        }
        return mapToDTO(questionRepository.findAllWithCountsOrderByLatest());
    }

    @Transactional(readOnly = true)
    public List<QuestionListDTO> search(String keyword) {
        return mapToDTO(questionRepository.searchWithCounts(keyword));
    }

    // ============================
    // 5. 찜(좋아요), 신고, 답변 등록
    // ============================
    @Transactional(readOnly = true)
    public boolean isFavorite(Long id, String userId) {
        return questionRepository.countFavorite(id, userId) > 0;
    }

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

    public void report(Long qnaId, String reason, String userId) {
        Question q = questionRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        q.setReportCount((q.getReportCount() == null ? 0 : q.getReportCount()) + 1);
        questionRepository.save(q);

        QuestionReport report = new QuestionReport();
        report.setQnaId(qnaId);
        report.setUserId(userId);
        report.setReason(reason);
        reportRepository.save(report);
    }

    public void registerReply(Long id, String content, String userId) {
        questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        ReplyEntity entity = new ReplyEntity();
        entity.setQuestionId(id);
        entity.setUserId(userId);
        entity.setContent(content);
        entity.setCreatedAt(LocalDateTime.now());
        replyRepository.save(entity);

        Question q = questionRepository.findById(id).get();
        q.setAnswered(1);
        questionRepository.save(q);
    }

    @Transactional(readOnly = true)
    public List<ReplyEntity> getReplies(Long questionId) {
        return replyRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    // ============================
    // 6. DTO 매핑
    // ============================
    private List<QuestionListDTO> mapToDTO(List<Object[]> rows) {
        return rows.stream().map(this::basicMapping).toList();
    }

    private QuestionListDTO basicMapping(Object[] row) {
        // 인덱스: 0:id, 1:userId, 2:title, 3:content, 4:answered, 5:reportCount, 6:createdAt, 7:replyCount, 8:favoriteCount, 9:deleted
        Long id = row[0] == null ? 0L : ((Number) row[0]).longValue();
        String userId = row[1] == null ? "" : row[1].toString();
        String title = row[2] == null ? "" : row[2].toString();
        String content = row[3] == null ? "" : row[3].toString();
        Integer answered = row[4] == null ? 0 : ((Number) row[4]).intValue();
        Integer reportCount = row[5] == null ? 0 : ((Number) row[5]).intValue();

        LocalDateTime createdAt;
        if (row[6] instanceof Timestamp ts) createdAt = ts.toLocalDateTime();
        else if (row[6] instanceof LocalDateTime ldt) createdAt = ldt;
        else createdAt = LocalDateTime.now();

        Integer replyCount = row[7] == null ? 0 : ((Number) row[7]).intValue();
        Integer favoriteCount = row[8] == null ? 0 : ((Number) row[8]).intValue();
        Integer deleted = (row.length > 9 && row[9] != null) ? ((Number) row[9]).intValue() : 0;

        return new QuestionListDTO(
                id, title, content, userId, answered, reportCount,
                favoriteCount, createdAt, deleted, replyCount, null
        );
    }

    @Transactional(readOnly = true)
    public int getReplyCount(Long id) {
        return replyRepository.countByQuestionId(id);
    }

    @Transactional(readOnly = true)
    public int getFavoriteCount(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return question.getFavoriteCount() != null ? question.getFavoriteCount() : 0;
    }
}