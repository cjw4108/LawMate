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
    // 최신순
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByCreatedAtDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByLatest()
        );
    }

    // ============================
    // 답변 많은 순
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByReplyCountDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByReply()
        );
    }

    // ============================
    // 좋아요 많은 순
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findAllByOrderByLikesDesc() {
        return mapToDTO(
                questionRepository.findAllWithCountsOrderByLikes()
        );
    }

    // ============================
    // 제목 검색
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> search(String keyword) {

        List<Question> questions =
                questionRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);

        return questions.stream()
                .map(q -> new QuestionListDTO(
                        q.getId(),
                        q.getUserId(),
                        q.getTitle(),
                        q.getContent(),
                        q.getAnswered() == null ? 0 : q.getAnswered(),
                        q.getReportCount() == null ? 0 : q.getReportCount(),
                        q.getCreatedAt(),
                        replyRepository.countByQuestionId(q.getId()),
                        questionRepository.countFavoriteByQuestion(q.getId())
                ))
                .toList();
    }

    // ============================
    // 내가 찜한 글
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> findMyFavorites(String userId) {

        List<Question> questions = questionRepository.findMyFavorites(userId);

        return questions.stream()
                .map(q -> new QuestionListDTO(
                        q.getId(),
                        q.getUserId(),
                        q.getTitle(),
                        q.getContent(),
                        q.getAnswered() == null ? 0 : q.getAnswered(),
                        q.getReportCount() == null ? 0 : q.getReportCount(),
                        q.getCreatedAt(),
                        replyRepository.countByQuestionId(q.getId()),
                        questionRepository.countFavoriteByQuestion(q.getId())
                ))
                .toList();
    }

    // ============================
    // 단건 조회
    // ============================
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    // ============================
    // 🔥 통합 리스트
    // ============================
    @Transactional(readOnly = true)
    public List<QuestionListDTO> getList(String keyword, String sort, String userId) {

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

    // ============================
    // 찜 관련
    // ============================
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

    // ============================
    // 신고
    // ============================
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

    // ============================
    // 답변 등록
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
    }

    @Transactional(readOnly = true)
    public List<ReplyEntity> getReplies(Long questionId) {
        return replyRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    @Transactional(readOnly = true)
    public int getReplyCount(Long questionId) {
        return replyRepository.countByQuestionId(questionId);
    }

    // ============================
    // 신고된 글 목록 (관리자)
    // ============================
    @Transactional(readOnly = true)
    public List<Question> findReportedQuestions() {
        return questionRepository
                .findByReportCountGreaterThanOrderByReportCountDesc(0);
    }

    // ============================
    // 🔥 Object[] → DTO 안전 매핑
    // ============================
    private List<QuestionListDTO> mapToDTO(List<Object[]> rows) {

        return rows.stream().map(row -> {

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
                    id,
                    userId,
                    title,
                    content,
                    answered,
                    reportCount,
                    createdAt,
                    replyCount,
                    favoriteCount
            );
        }).toList();
    }
}