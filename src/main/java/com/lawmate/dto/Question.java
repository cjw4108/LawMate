package com.lawmate.dto;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTIONS")
@Getter @Setter
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

    @Column(name = "REPORT_COUNT")
    private Integer reportCount = 0;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.answered == null) this.answered = 0;
        if (this.reportCount == null) this.reportCount = 0;
    }
}