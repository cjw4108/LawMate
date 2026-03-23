package com.lawmate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
public class UserDTO {
    // 1. 기본 계정 정보
    private int id;
    private String userId;
    private String password;
    private String name;
    private String phone;
    private String email;

    // 2. 권한 및 상태 정보
    private String role = "ROLE_USER";
    private String status = "정상";

    // 3. 변호사 관련 정보
    private String lawyerStatus = "NONE";
    private String licenseFile;
    private String specialty;
    private String introduction; // 변호사 소개 추가
    private String rejectReason;
    private int applyCount = 0;

    // 4. 가입일 — JSON 직렬화 제외
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate = LocalDate.now();

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", userId=" + userId + ", name=" + name + ", phone=" + phone +
                ", role=" + role + ", status=" + status + ", lawyerStatus=" + lawyerStatus + "]";
    }
}
