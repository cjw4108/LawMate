package com.lawmate.service;

import com.lawmate.dao.adminDAO;
import com.lawmate.dto.adminDTO;
import org.springframework.stereotype.Service;

@Service
public class adminService {

    private final adminDAO adminDAO;

    public adminService(adminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public adminDTO login(String adminId, String adminPw) {
        return adminDAO.login(adminId, adminPw);
    }
}