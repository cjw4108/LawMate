package com.lawmate.service;

import com.lawmate.dao.AdminLawyerDAO;
import com.lawmate.dao.UserDAO;
import com.lawmate.dto.LawyerApprovalDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminLawyerService {

    private final AdminLawyerDAO adminLawyerDAO;
    private final UserDAO userDAO;

    public AdminLawyerService(AdminLawyerDAO adminLawyerDAO, UserDAO userDAO) {
        this.adminLawyerDAO = adminLawyerDAO;
        this.userDAO = userDAO;
    }

    // ✅ 파라미터 3개를 받도록 수정
    public List<LawyerApprovalDTO> getPendingLawyers(String role, String status, String keyword) {
        return adminLawyerDAO.findPendingLawyers(role, status, keyword);
    }

    public List<LawyerApprovalDTO> getAllLawyers() {
        return adminLawyerDAO.findAllLawyers();
    }

    public LawyerApprovalDTO getLawyerDetail(String lawyerId) {
        return adminLawyerDAO.findByLawyerId(lawyerId);
    }

    @Transactional
    public void updateLawyerStatus(String userId, String status, String rejectReason) {
        // 단일 쿼리로 처리하는 최신 로직 유지
        adminLawyerDAO.updateStatus(userId, status, rejectReason);
    }
}