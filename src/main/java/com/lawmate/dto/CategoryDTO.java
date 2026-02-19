package com.lawmate.dto;

import java.time.LocalDateTime;

public class CategoryDTO {
    private int categoryId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private long totalViewCount; // 추가

    // MyBatis를 위한 기본 생성자 필수!
    public CategoryDTO() {}

    public CategoryDTO(int categoryId, String name, String description, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public CategoryDTO(int categoryId, String name, String description, LocalDateTime createdAt, long totalViewCount) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.totalViewCount = totalViewCount;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getTotalViewCount() { return totalViewCount; }
    public void setTotalViewCount(long totalViewCount) { this.totalViewCount = totalViewCount; }
}