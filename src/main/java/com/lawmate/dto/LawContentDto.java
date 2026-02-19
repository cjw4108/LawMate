package com.lawmate.dto;

import java.util.Date;

public class LawContentDto {
    private int contentId;
    private int categoryId;
    private String title;
    private String summary;
    private String content;      // CLOB - 상세 내용
    private String process;      // CLOB - 절차
    private String documents;    // CLOB - 필요 서류
    private int viewCount;
    private Date createdAt;
    private Date updatedAt;
    private String deepCategory;


    // JOIN으로 가져올 필드
    private String categoryName;

    // 기본 생성자
    public LawContentDto() {}

    // 전체 필드 생성자
    public LawContentDto(int contentId, int categoryId, String title, String summary,
                         String content, String process, String documents,
                         int viewCount, Date createdAt, Date updatedAt,
                         String deepCategory, String categoryName) {
        this.contentId = contentId;
        this.categoryId = categoryId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.process = process;
        this.documents = documents;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deepCategory = deepCategory;
        this.categoryName = categoryName;
    }

    // Getter & Setter
    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeepCategory() {
        return deepCategory;
    }

    public void setDeepCategory(String deepCategory) {
        this.deepCategory = deepCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "LawContentDto{" +
                "contentId=" + contentId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", deepCategory='" + deepCategory + '\'' +
                ", viewCount=" + viewCount +
                ", updatedAt=" + updatedAt +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}