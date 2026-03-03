package com.lawmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS") // DB 테이블명과 일치시켜야 합니다. [cite: 8]
@Data
public class UserEntity {

    @Id
    @Column(name = "USER_ID") // 이미지의 USER_ID 컬럼 [cite: 8]
    private String userId;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE") // 일반/변호사 구분 [cite: 4]
    private String role;

    @Column(name = "STATUS") // 정상/정지 상태 [cite: 2, 5]
    private String status;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // 추가로 이미지에 있는 변호사 승인 상태 등이 필요하면 아래 추가
    @Column(name = "LAWYER_STATUS")
    private String lawyerStatus;
}