package com.lawmate.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QUESTION_FAVORITES") // 실제 DB 테이블명과 일치시키세요
@Getter @Setter
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QNA_ID")
    private Long qnaId;

    @Column(name = "USER_ID")
    private String userId;
}