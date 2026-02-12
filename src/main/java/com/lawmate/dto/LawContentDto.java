package com.lawmate.dto;

import java.time.LocalDateTime;

public class LawContentDto {
    private int contentId;
    private int categoryId;
    private String deepCategory;
    private String title;
    private String summary;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성자
    public LawContentDto() {}

    public LawContentDto(int contentId, int categoryId, String deepCategory,
                         String title, String summary, int viewCount,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.contentId = contentId;
        this.categoryId = categoryId;
        this.deepCategory = deepCategory;
        this.title = title;
        this.summary = summary;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getDeepCategory() {
        return deepCategory;
    }

    public void setDeepCategory(String deepCategory) {
        this.deepCategory = deepCategory;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}