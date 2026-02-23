package com.lawmate.service;

import com.lawmate.dao.AdminDAO;
import com.lawmate.dto.AdminDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminDAO AdminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.AdminDAO = adminDAO;
    }

    public static AdminDTO login(AdminDTO adminDTO) {
        return adminDTO;
    }

    public AdminDTO login(String adminId, String adminPw) {
        return AdminDAO.login(adminId, adminPw);
    }
}