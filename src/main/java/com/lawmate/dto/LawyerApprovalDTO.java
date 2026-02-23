package com.lawmate.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LawyerApprovalDTO {

    private String lawyerId;        // 변호사 아이디
    private String lawyerName;      // 변호사 이름
    private String email;           // 이메일
    private String status;          // PENDING, APPROVED, REJECTED
    private String licenseFile;     // 자격증 파일 경로
    private String rejectReason;    // 반려 사유
    private int applyCount;         // 신청 횟수
    private LocalDate applyDate;    // 신청일
    private LocalDate approveDate;  // 승인일
}
