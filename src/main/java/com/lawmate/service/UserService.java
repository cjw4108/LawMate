package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    // ✅ : 프로젝트 내부의 static/uploads 폴더를 경로로 지정
    private final String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    // 1. 일반 회원가입
    @Transactional
    public boolean signup(UserDTO user) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        user.setRole("ROLE_USER");
        user.setStatus("정상");

        userDAO.signup(user);
        return true;
    }

    // 2. 변호사 회원가입
    @Transactional
    public boolean signupLawyer(UserDTO user, MultipartFile licenseFile) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }
// ✅ 자격증 파일이 있을 경우 저장 처리
        if (licenseFile != null && !licenseFile.isEmpty()) {
            try {
                String savedName = saveFile(licenseFile);
                user.setLicenseFile(savedName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        user.setRole("ROLE_LAWYER");
        user.setStatus("승인대기"); // 로그인 시 '승인대기' 상태면 차단되도록 컨트롤러에서 처리됨
        user.setLawyerStatus("PENDING"); // 관리자 승인 페이지 목록용

        return userDAO.saveLawyer(user) > 0;
    }
    // 3. 로그인 (정지 계정 차단 로직 추가)
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }
    // 4. 관리자용: 유저 상태 변경 (정상 <-> 정지)
    @Transactional
    public void changeStatus(String userId, String status) {
        // 이 기능을 위해 UserDAO에 updateStatus(userId, status) 메서드가 필요합니다.
        userDAO.updateStatus(userId, status);
    }

    public UserDTO getUserById(String userId) {
        return userDAO.findByUserId(userId);
    }

    @Transactional
    public void updateProfile(UserDTO userDTO) {
        userDAO.updateProfile(userDTO);
    }

    // ✅ 파일 저장 유틸리티 (경로 생성 로직 포함)
    private String saveFile(MultipartFile file) throws Exception {
        // 폴더가 없으면 생성
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일명 중복 방지를 위한 UUID 처리
        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 실제 파일 저장
        File destination = new File(uploadPath + savedName);
        file.transferTo(destination);

        return savedName;
    }
}