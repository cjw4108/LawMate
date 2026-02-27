package com.lawmate.dto;

import java.time.LocalDateTime;

public class QuestionListDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;          // JSP에서 사용
    private Integer answered;
    private Integer reportCount;
    private Integer favoriteCount;
    private LocalDateTime createdAt;
    private Integer deleted;
    private String reportReason;    // 모달용

    // 기본 생성자
    public QuestionListDTO() {}

    // 전체 생성자
    public QuestionListDTO(Long id, String title, String content, String writer,
                           Integer answered, Integer reportCount, Integer favoriteCount,
                           LocalDateTime createdAt, Integer deleted, String reportReason) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.answered = answered;
        this.reportCount = reportCount;
        this.favoriteCount = favoriteCount;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.reportReason = reportReason;
    }

    // ================= Getter / Setter =================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }

    public Integer getAnswered() { return answered; }
    public void setAnswered(Integer answered) { this.answered = answered; }

    public Integer getReportCount() { return reportCount; }
    public void setReportCount(Integer reportCount) { this.reportCount = reportCount; }

    public Integer getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public String getReportReason() { return reportReason; }
    public void setReportReason(String reportReason) { this.reportReason = reportReason; }
}