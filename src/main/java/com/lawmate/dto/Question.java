package com.lawmate.dto;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTIONS")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @Column(name = "USER_ID")
    private String userId; // USER_ID

    private String title; // TITLE

    @Lob
    private String content; // CONTENT

    private Integer answered = 0; // ANSWERED

    @Column(name = "ADOPTED_ANSWER")
    private Long adoptedAnswer; // ADOPTED_ANSWER

    @Column(name = "REPORT_COUNT")
    private Integer reportCount = 0; // REPORT_COUNT

    @Column(name = "FAVORITE_COUNT")
    private Integer favoriteCount = 0; // FAVORITE_COUNT

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt; // CREATED_AT

    @Column(name = "DELETED")
    private Integer deleted = 0; // DELETED

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.answered == null) this.answered = 0;
        if (this.reportCount == null) this.reportCount = 0;
        if (this.favoriteCount == null) this.favoriteCount = 0;
        if (this.deleted == null) this.deleted = 0;
    }
}