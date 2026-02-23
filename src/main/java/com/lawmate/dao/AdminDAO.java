package com.lawmate.dao;

import com.lawmate.dto.AdminDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDAO {

    private final List<AdminDTO> admins = new ArrayList<>();

    public AdminDAO() {
        AdminDTO admin = new AdminDTO();
        admin.setAdminId("admin");
        admin.setAdminPw("1234");
        admin.setAdminName("관리자");

        admins.add(admin);
    }

    public AdminDTO login(String adminId, String adminPw) {
        for (AdminDTO admin : admins) {
            if (admin.getAdminId().equals(adminId)
                    && admin.getAdminPw().equals(adminPw)) {
                return admin;
            }
        }
        return null;
    }
}