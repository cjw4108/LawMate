package com.lawmate.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LawyerApprovalDTO {
    // JSP에서 요구하는 모든 필드를 추가해야 합니다.
    private String userId;      // 사용자 ID
    private String userName;    // 사용자 이름
    private String licenseFile; // 자격증 파일 경로 또는 이름
    private String status;      // 승인 상태 (PENDING 등)
    private String role;        // 권한 (변호사 등)

    // 추가로 필요한 필드가 있다면 여기에 더 작성하세요.
    private LocalDate requestDate;
}
