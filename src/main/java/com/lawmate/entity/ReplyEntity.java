package com.lawmate.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTION_REPLIES")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ")
    @SequenceGenerator(name = "REPLY_SEQ", sequenceName = "REPLY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QNA_ID")
    private Long questionId;

    @Column(name = "USER_ID")
    private String userId;   // ✅ 수정 완료

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}