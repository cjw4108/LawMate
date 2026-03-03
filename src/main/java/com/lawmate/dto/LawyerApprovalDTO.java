package com.lawmate.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LawyerApprovalDTO {
    private String userId;
    private String userName;
    private String userPhone;     // 추가
    private String licenseFile;
    private String lawyerStatus;  // 추가 (status 대신)
    private String status;
    private String role;
    private LocalDate requestDate;
}