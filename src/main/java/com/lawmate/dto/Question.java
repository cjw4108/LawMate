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
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ANSWERED")
    private Integer answered;

    @Column(name = "ADOPTED_ANSWER")
    private Long adoptedAnswer;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.answered == null) this.answered = 0;
    }
}
