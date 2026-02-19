package com.lawmate.dto;

import java.util.Date;

public class ConsultDto {
    private int id;
    private String userId;
    private String title;
    private String content;
    private int answered;
    private int adoptedAnswer;
    private Date createdAt;
    public ConsultDto() {
        // TODO Auto-generated constructor stub
    }
    public ConsultDto(int id, String userId, String title, String content, int answered, int adoptedAnswer,
                   Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.answered = answered;
        this.adoptedAnswer = adoptedAnswer;
        this.createdAt = createdAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getAnswered() {
        return answered;
    }
    public void setAnswered(int answered) {
        this.answered = answered;
    }
    public int getAdoptedAnswer() {
        return adoptedAnswer;
    }
    public void setAdoptedAnswer(int adoptedAnswer) {
        this.adoptedAnswer = adoptedAnswer;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
