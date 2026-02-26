package com.lawmate.service;

import com.lawmate.dao.AdminDAO;
import com.lawmate.dto.AdminDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    /**
     * 관리자 로그인 로직 (테스트 계정 우선 확인 버전)
     */
    public AdminDTO login(AdminDTO adminDTO) {
        // 1. 테스트용 하드코딩 계정 체크 (DB에 데이터가 없어도 로그인 가능)
        if ("admin".equals(adminDTO.getAdminId()) && "1234".equals(adminDTO.getAdminPw())) {
            AdminDTO testAdmin = new AdminDTO();
            testAdmin.setAdminId("admin");
            testAdmin.setAdminPw("1234");
            testAdmin.setAdminName("임시관리자");
            return testAdmin; // 일치하면 DB 조회 없이 즉시 객체 리턴
        }

        // 2. 테스트 계정이 아닐 경우 실제 DB(USERS 테이블) 조회
        return adminDAO.login(adminDTO.getAdminId(), adminDTO.getAdminPw());
    }

    /**
     * 아이디와 비밀번호를 직접 전달받는 버전 (오버로딩)
     */
    public AdminDTO login(String adminId, String adminPw) {
        // 테스트용 하드코딩 계정 체크
        if ("admin".equals(adminId) && "1234".equals(adminPw)) {
            AdminDTO testAdmin = new AdminDTO();
            testAdmin.setAdminId("admin");
            testAdmin.setAdminPw("1234");
            testAdmin.setAdminName("임시관리자");
            return testAdmin;
        }

        // 실제 DB 조회
        return adminDAO.login(adminId, adminPw);
    }
}