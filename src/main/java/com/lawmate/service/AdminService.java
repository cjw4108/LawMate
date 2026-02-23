package com.lawmate.service;

import com.lawmate.dao.adminDAO;
import com.lawmate.dto.adminDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final adminDAO adminDAO;

    public AdminService(adminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public static adminDTO login(adminDTO adminDTO) {
        return adminDTO;
    }

    public adminDTO login(String adminId, String adminPw) {
        return adminDAO.login(adminId, adminPw);
    }
}