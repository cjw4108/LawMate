package com.lawmate.dto;

import lombok.Data;

@Data
public class UserDTO {
    // 공통 필드
    private String userId;          // 아이디
    private String password;        // 비밀번호
    private String passwordConfirm; // 비밀번호 확인용
    private String userName;        // 성함 (추가됨)
    private String email;           // 이메일 주소
    private String role;            // ROLE_USER, ROLE_LAWYER, ROLE_ADMIN

    // 변호사 및 승인 관련 필드
    private String lawyerStatus;    // PENDING, APPROVED, REJECTED
    private String licenseFile;     // 자격증 파일명
    private String specialty;       // 전문 분야
    private String rejectReason;    // 반려 사유 (관리자용)

    @Override
    public String toString() {
        return "UserDTO [userId=" + userId + ", role=" + role + ", status=" + lawyerStatus + "]";
    }
}