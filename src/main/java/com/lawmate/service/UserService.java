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

    private final String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    // ✅ 추가: 아이디 중복 체크 로직 (컨트롤러에서 호출함)
    public boolean idCheck(String userId) {
        // 0보다 크면 중복(true), 0이면 사용 가능(false)
        return userDAO.existsByUserId(userId) > 0;
    }

    // 1. 일반 회원가입 (로직 보강)
    @Transactional
    public boolean signup(UserDTO user) {
        // 이미 가입된 아이디인지 확인
        if (idCheck(user.getUserId())) {
            return false;
        }

        user.setRole("ROLE_USER");
        user.setStatus("정상");

        // 가입 성공 여부를 반환하도록 수정 (DAO가 영향을 받은 행의 수를 반환한다고 가정)
        userDAO.signup(user);
        return true;
    }

    // 2. 변호사 회원가입
    @Transactional
    public boolean signupLawyer(UserDTO user, MultipartFile licenseFile) {
        if (idCheck(user.getUserId())) {
            return false;
        }

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
        user.setStatus("승인대기");
        user.setLawyerStatus("PENDING");

        return userDAO.saveLawyer(user) > 0;
    }

    // 3. 로그인
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }

    // 4. 관리자용: 유저 상태 변경
    @Transactional
    public void changeStatus(String userId, String status) {
        userDAO.updateStatus(userId, status);
    }

    public UserDTO getUserById(String userId) {
        return userDAO.findByUserId(userId);
    }

    @Transactional
    public void updateProfile(UserDTO userDTO) {
        userDAO.updateProfile(userDTO);
    }

    private String saveFile(MultipartFile file) throws Exception {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File destination = new File(uploadPath + savedName);
        file.transferTo(destination);

        return savedName;
    }
}