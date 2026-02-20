package com.lawmate.dto;

public class UserDTO {

    private String userId;
    private String password;
    private String email;
    private String role; // USER / LAWYER / ADMIN

    // 변호사용
    private String lawyerStatus; // PENDING / APPROVED / REJECTED
    private String licenseFile;

    // getter / setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLawyerStatus() { return lawyerStatus; }
    public void setLawyerStatus(String lawyerStatus) { this.lawyerStatus = lawyerStatus; }

    public String getLicenseFile() { return licenseFile; }
    public void setLicenseFile(String licenseFile) { this.licenseFile = licenseFile; }
}