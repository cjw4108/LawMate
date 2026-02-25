package com.lawmate.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String userId;
    private String password;
    private String passwordConfirm;
    private String userName;   // 매퍼의 userName과 매칭
    private String nickname;
    private String email;

    private String lawyerStatus;   // 🔥 Lombok이 getter/setter 자동 생성

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate = LocalDate.now();

    private String role = "ROLE_USER";

    // DB STATUS 컬럼 매칭
    private String status = "ACTIVE";

    private int applyCount = 0;

    // DB LICENSE_FILE 컬럼 매칭
    private String licenseFile;

    private String specialty;
    private String rejectReason;

    @Override
    public String toString() {
        return "UserDTO [userId=" + userId +
                ", role=" + role +
                ", status=" + status + "]";
    }
}