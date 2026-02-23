package com.lawmate.service;

import com.lawmate.dao.AdminLawyerDAO;
import com.lawmate.dto.LawyerApprovalDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class adminLawyerService {

    private final AdminLawyerDAO adminLawyerDAO;

    public adminLawyerService(AdminLawyerDAO adminLawyerDAO) {
        this.adminLawyerDAO = adminLawyerDAO;
    }

    public List<LawyerApprovalDTO> getPendingLawyers() {
        return adminLawyerDAO.findPendingLawyers();
    }

    public void approveLawyer(String lawyerId) {
        adminLawyerDAO.approve(lawyerId);
    }

    public void rejectLawyer(String lawyerId) {
        adminLawyerDAO.reject(lawyerId);
    }
}