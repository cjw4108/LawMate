package com.lawmate.service;

import com.lawmate.dao.AdminLawyerDAO;
import com.lawmate.dto.LawyerApprovalDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLawyerService {

    private final AdminLawyerDAO adminLawyerDAO;

    public AdminLawyerService(AdminLawyerDAO adminLawyerDAO) {
        this.adminLawyerDAO = adminLawyerDAO;
    }

    // 승인 대기 목록 조회
    public List<LawyerApprovalDTO> getPendingLawyers() {
        return adminLawyerDAO.findPendingLawyers();
    }

    // 전체 변호사 목록 조회
    public List<LawyerApprovalDTO> getAllLawyers() {
        return adminLawyerDAO.findAllLawyers();
    }

    // 변호사 상세 조회
    public LawyerApprovalDTO getLawyerDetail(String lawyerId) {
        return adminLawyerDAO.findByLawyerId(lawyerId);
    }

    // 승인 처리
    public void approveLawyer(String lawyerId) {
        adminLawyerDAO.approve(lawyerId);
    }

    // 반려 처리
    public void rejectLawyer(String lawyerId, String rejectReason) {
        adminLawyerDAO.reject(lawyerId, rejectReason);
    }

    // 상태 업데이트 (승인/반려 통합)
    public void updateLawyerStatus(String lawyerId, String status, String rejectReason) {
        adminLawyerDAO.updateStatus(lawyerId, status, rejectReason);
    }

    // ✅ 수정: static 메서드 삭제
}