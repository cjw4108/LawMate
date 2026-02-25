package com.lawmate.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class UserDTO {
    private String userId;
    private String password;
    private String passwordConfirm;
    private String userName; // 매퍼의 userName과 매칭
    private String nickname;
    private String email;
    private String lawyerStatus;

    public String getLawyerStatus() {
        return lawyerStatus;
    }

    public void setLawyerStatus(String lawyerStatus) {
        this.lawyerStatus = lawyerStatus;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate = LocalDate.now();

    private String role = "ROLE_USER";

    // 🔴 중요: DB의 STATUS 컬럼과 매칭하기 위해 이름을 status로 변경합니다.
    private String status = "ACTIVE";

    private int applyCount = 0;

    // 🔴 중요: DB의 LICENSE_FILE 컬럼과 매칭 (매퍼의 licenseFile과 동일)
    private String licenseFile;

    private String specialty;
    private String rejectReason;

    @Override
    public String toString() {
        return "UserDTO [userId=" + userId + ", role=" + role + ", status=" + status + "]";
    }

    public Object getLawyerStatus() {
        return null;
    }

    public void setLawyerStatus(String none) {
    }
}