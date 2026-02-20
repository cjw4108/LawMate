package com.lawmate.dao;

import com.lawmate.dto.adminDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class adminDAO {

    private final List<adminDTO> admins = new ArrayList<>();

    public adminDAO() {
        adminDTO admin = new adminDTO();
        admin.setAdminId("admin");
        admin.setAdminPw("1234");
        admin.setAdminName("관리자");

        admins.add(admin);
    }

    public adminDTO login(String adminId, String adminPw) {
        for (adminDTO admin : admins) {
            if (admin.getAdminId().equals(adminId)
                    && admin.getAdminPw().equals(adminPw)) {
                return admin;
            }
        }
        return null;
    }
}