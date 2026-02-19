package com.lawmate.dto;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTION_REPORTS")
@Getter @Setter
@NoArgsConstructor
public class QuestionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QNA_ID")
    private Long qnaId;

    @Column(name = "USER_ID")
    private String userId;

    private String reason;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}