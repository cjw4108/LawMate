package com.lawmate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
}