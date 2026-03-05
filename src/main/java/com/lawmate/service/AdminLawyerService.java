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

    public List<LawyerApprovalDTO> getPendingLawyers(String keyword) {
        return adminLawyerDAO.findPendingLawyers(keyword);
    }

    public List<LawyerApprovalDTO> getAllLawyers() {
        return adminLawyerDAO.findAllLawyers();
    }

    public LawyerApprovalDTO getLawyerDetail(String lawyerId) {
        return adminLawyerDAO.findByLawyerId(lawyerId);
    }

    public void approveLawyer(String lawyerId) {
        adminLawyerDAO.approve(lawyerId);
    }

    public void rejectLawyer(String lawyerId, String rejectReason) {
        adminLawyerDAO.reject(lawyerId, rejectReason);
    }

    @Transactional
    public void updateLawyerStatus(String userId, String status, String rejectReason) {
        // ✅ 이 메서드 하나면 충분합니다!
        // AdminLawyerMapper.xml의 updateStatus 쿼리 안에
        // LAWYER_STATUS, ROLE, STATUS('정상') 업데이트 로직이 모두 들어있습니다.
        adminLawyerDAO.updateStatus(userId, status, rejectReason);

        // ❌ 기존에 에러를 유발하던 userDAO.updateStatus(userId, "정상"); 부분은 삭제했습니다.
    }
}