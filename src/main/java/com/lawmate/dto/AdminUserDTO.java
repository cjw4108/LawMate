package com.lawmate.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AdminUserDTO {
    private String userId;
    private String name;
    private String role;     // 일반(USER), 변호사(LAWYER)
    private String status;   // 정상, 정지
    private Date createdAt;
}