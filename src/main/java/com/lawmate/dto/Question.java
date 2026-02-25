package com.lawmate.dto;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    private String title;

    @Lob
    private String content;

    private Integer answered = 0;

    @Column(name = "ADOPTED_ANSWER")
    private Long adoptedAnswer;

    // 신고 횟수
    @Column(name = "REPORT_COUNT", nullable = false)
    private Integer reportCount = 0;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // DB에는 없고 화면 표시용
    @Transient
    private Integer replyCount;

    // DB에는 없고 화면 표시용 (좋아요 수)
    @Transient
    private Integer favoriteCount;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.answered == null) this.answered = 0;
        if (this.reportCount == null) this.reportCount = 0;
    }
}