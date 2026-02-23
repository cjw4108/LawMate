package com.lawmate.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class UserDTO {
    // 사용자가 직접 입력하는 필드
    private String userId;
    private String password;
    private String passwordConfirm;
    private String userName;
    private String nickname;
    private String email;

    // 🔴 여기서부터 '자동'으로 값이 채워지는 설정입니다.

    // 1. 가입일: 객체 생성 시 자동으로 오늘 날짜 입력
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate = LocalDate.now();

    // 2. 권한: 기본값을 ROLE_USER로 설정 (변호사 가입 시에만 컨트롤러에서 덮어씀)
    private String role = "ROLE_USER";

    // 3. 변호사 상태: 기본값 NONE
    private String lawyerStatus = "NONE";

    // 4. 신청 횟수: 기본값 0
    private int applyCount = 0;

    // 기타 변호사 전용 필드
    private String licenseFile;
    private String specialty;
    private String rejectReason;

    @Override
    public String toString() {
        return "UserDTO [userId=" + userId + ", role=" + role + ", joinDate=" + joinDate + "]";
    }
}