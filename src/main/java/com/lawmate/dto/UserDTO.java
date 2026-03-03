package com.lawmate.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class UserDTO {
    // 1. 기본 계정 정보
    private String userId;
    private String password;
    private String name;   // JSP의 name="name"과 매칭
    private String phone;  // JSP의 name="phone"과 매칭
    private String email;

    // 2. 권한 및 상태 정보 (중복 제거)
    private String role = "ROLE_USER";
    private String status = "정상";

    /// 3. 변호사 관련 정보 (중복 제거)
    private String lawyerStatus = "NONE";
    private String licenseFile;
    private String specialty;
    private String rejectReason;
    private int applyCount = 0;

    // 4. 가입일 (타입 통일 및 초기화)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate = LocalDate.now();

    // @Data가 있으면 toString()을 직접 적을 필요가 없습니다.
    // 만약 직접 적고 싶다면 아래처럼 필드명이 일치해야 합니다.
    @Override
    public String toString() {
        return "UserDTO [userId=" + userId + ", name=" + name + ", phone=" + phone +
                ", role=" + role + ", status=" + status + ", lawyerStatus=" + lawyerStatus + "]";
    }
}