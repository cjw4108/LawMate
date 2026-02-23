package com.lawmate.dto;

/**
 * 일반 회원 및 변호사 정보를 통합 관리하는 DTO
 */
public class UserDTO {

    // 1. 공통 필드
    private String userId;       // 사용자 아이디
    private String password;     // 비밀번호
    private String passwordConfirm; // 비밀번호 확인 (회원가입 시 검증용)
    private String email;        // 이메일 주소
    private String userName;     // 사용자 이름 (추가 권장)
    private String role;         // 권한 구분: ROLE_USER(일반), ROLE_LAWYER(변호사), ROLE_ADMIN(관리자)

    // 2. 변호사 전용 필드
    private String lawyerStatus; // 승인 상태: PENDING(대기), APPROVED(승인), REJECTED(반려)
    private String licenseFile;  // 서버에 저장된 자격증 파일명 (UUID_파일명 형식)
    private String specialty;    // 전문 분야 (민사, 형사 등)

    // 기본 생성자
    public UserDTO() {}

    // Getter / Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLawyerStatus() { return lawyerStatus; }
    public void setLawyerStatus(String lawyerStatus) { this.lawyerStatus = lawyerStatus; }

    public String getLicenseFile() { return licenseFile; }
    public void setLicenseFile(String licenseFile) { this.licenseFile = licenseFile; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    // 디버깅을 위한 toString 추가 (로그 확인 시 편리함)
    @Override
    public String toString() {
        return "UserDTO [userId=" + userId + ", role=" + role + ", status=" + lawyerStatus + "]";
    }
}