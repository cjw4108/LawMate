package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final String uploadPath = "C:/lawmate/uploads/";

    // 1. 일반 회원가입
    @Transactional
    public boolean signup(UserDTO user) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        // 권한 설정
        user.setRole("ROLE_USER");

        // 🔴 [수정] lawyerStatus -> status로 변경 (DTO와 일치)
        user.setStatus("ACTIVE");

        userDAO.signup(user);
        return true;
    }

    // 2. 변호사 회원가입
    @Transactional
    public boolean signupLawyer(UserDTO user, MultipartFile licenseFile) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        if (licenseFile != null && !licenseFile.isEmpty()) {
            try {
                String savedName = saveFile(licenseFile);

                // 🔴 [수정] 이제 DB에 컬럼을 추가했으므로 DTO에 세팅해도 됩니다.
                user.setLicenseFile(savedName);

                System.out.println("변호사 증빙파일 저장 완료: " + savedName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // 변호사는 기본 권한 ROLE_LAWYER와 대기 상태 PENDING 설정
        user.setRole("ROLE_LAWYER");
        user.setStatus("PENDING");

        return userDAO.saveLawyer(user) > 0;
    }

    // 3. 로그인 및 기타 기능
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

    // 파일 저장 유틸리티 메서드
    private String saveFile(MultipartFile file) throws Exception {
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();
        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + savedName));
        return savedName;
    }
}