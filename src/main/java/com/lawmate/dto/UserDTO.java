package com.lawmate.dto;

public class UserDTO {

    private String userId;
    private String password;
    private String email;
    private String role;           // USER / LAWYER / ADMIN
    private String lawyerStatus;   // PENDING / APPROVED / REJECTED
    private String proofFilePath;  // 변호사 증빙 파일

    public UserDTO() {}

    public UserDTO(String userId, String password, String email, String role) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.role = role;

        if ("LAWYER".equals(role)) {
            this.lawyerStatus = "PENDING";
        } else {
            this.lawyerStatus = "NONE";
        }
    }

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

    public String getProofFilePath() { return proofFilePath; }
    public void setProofFilePath(String proofFilePath) { this.proofFilePath = proofFilePath; }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", lawyerStatus='" + lawyerStatus + '\'' +
                '}';
    }
}