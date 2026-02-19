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

    // 명시적으로 컬럼 이름을 지정하고 기본값을 0으로 설정합니다
    @Column(name = "REPORT_COUNT", nullable = false)
    private Integer reportCount = 0;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.answered == null) this.answered = 0;
        // null 방지를 위해 생성 시 0으로 초기화합니다
        if (this.reportCount == null) this.reportCount = 0;
    }
}