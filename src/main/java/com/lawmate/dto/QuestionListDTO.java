package com.lawmate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionListDTO {

    private Long id;
    private String userId;
    private String title;
    private String content;
    private Integer answered;
    private Integer reportCount;
    private LocalDateTime createdAt;
    private int replyCount;
    private int favoriteCount;

    // 🔹 관리자 확장 필드
    private Integer deleted;        // 0 or 1
    private String reportReason;    // 신고 사유

    // ================================
    // ✅ 일반 게시판용 생성자 (9개)
    // ================================
    public QuestionListDTO(Long id,
                           String userId,
                           String title,
                           String content,
                           Integer answered,
                           Integer reportCount,
                           LocalDateTime createdAt,
                           int replyCount,
                           int favoriteCount) {

        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.answered = answered;
        this.reportCount = reportCount;
        this.createdAt = createdAt;
        this.replyCount = replyCount;
        this.favoriteCount = favoriteCount;
    }

    // =====================================
    // ✅ 관리자 확장 생성자 (11개)
    // =====================================
    public QuestionListDTO(Long id,
                           String userId,
                           String title,
                           String content,
                           Integer answered,
                           Integer reportCount,
                           LocalDateTime createdAt,
                           int replyCount,
                           int favoriteCount,
                           Integer deleted,
                           String reportReason) {

        this(id, userId, title, content, answered, reportCount,
                createdAt, replyCount, favoriteCount);

        this.deleted = deleted;
        this.reportReason = reportReason;
    }
}