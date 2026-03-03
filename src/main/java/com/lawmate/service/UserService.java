package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final String uploadPath = "C:/lawmate/uploads/license/";

    // =========================================================
    // 1. 회원가입 기능
    // =========================================================

    /**
     * 일반 회원가입
     */
    @Transactional
    public boolean signup(UserDTO user) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }
        user.setRole("ROLE_USER");
        user.setStatus("정상");
        user.setLawyerStatus("NONE");
        userDAO.signup(user);
        return true;
    }

    /**
     * 변호사 회원가입 (파일 업로드 포함)
     */
    @Transactional
    public boolean signupLawyer(UserDTO user, MultipartFile licenseFile) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        // 1. 파일 저장 처리
        if (licenseFile != null && !licenseFile.isEmpty()) {
            try {
                String savedName = saveFile(licenseFile);
                user.setLicenseFile(savedName);
            } catch (Exception e) {
                throw new RuntimeException("파일 저장 중 오류 발생", e);
            }
        }

        // 2. 변호사 정보 설정
        user.setRole("ROLE_LAWYER");
        user.setStatus("정상");
        user.setLawyerStatus("PENDING");

        return userDAO.saveLawyer(user) > 0;
    }

    // =========================================================
    // 2. 관리자 전용 기능 (변호사 승인 관리)
    // =========================================================

    /**
     * 승인 대기 중인 변호사 목록 가져오기
     */
    public List<UserDTO> getPendingLawyers() {
        return userDAO.findPendingLawyers();
    }

    /**
     * 변호사 승인/반려 상태 업데이트
     */
    @Transactional
    public void updateLawyerStatus(String userId, String status, String rejectReason) {
        userDAO.updateLawyerStatus(userId, status, rejectReason);
    }

    // =========================================================
    // 3. 공통 기능 (로그인, 프로필 관리)
    // =========================================================

    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }

    public UserDTO getUserById(String userId) {
        return userDAO.findByUserId(userId);
    }

    @Transactional
    public void updateProfile(UserDTO userDTO) {
        userDAO.updateProfile(userDTO);
    }

    // =========================================================
    // 4. 유틸리티 메서드
    // =========================================================

    /**
     * 파일 로컬 저장소 저장
     */
    private String saveFile(MultipartFile file) throws Exception {
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // transferTo() 대신 Files.copy() 사용 (임시파일 삭제 문제 방지)
        Files.copy(
                file.getInputStream(),
                Paths.get(uploadPath + savedName),
                StandardCopyOption.REPLACE_EXISTING
        );

        return savedName;
    }
}