package com.lawmate.service;

import com.lawmate.dao.LawyerDAO;
import com.lawmate.dao.UserDAO;
import com.lawmate.dto.LawyerDTO;
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
    private final LawyerDAO lawyerDAO;

    private final String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    // 유저 단건 조회
    public UserDTO findByUserId(String userId) {
        return userDAO.findByUserId(userId);
    }

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

    public boolean isUserIdExists(String userId) {
        return userDAO.existsByUserId(userId) > 0;
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

    // 5. ID로 유저 조회
    public UserDTO getUserById(String userId) {
        return userDAO.findByUserId(userId);
    }

    // 6. 프로필 수정
    @Transactional
    public void updateProfile(UserDTO userDTO) {
        // ① USERS 테이블 수정
        userDAO.updateProfile(userDTO);

        // ② 변호사면 TB_LAWYER도 동기화
        if ("ROLE_LAWYER".equals(userDTO.getRole())) {
            LawyerDTO lawyerDTO = new LawyerDTO();
            lawyerDTO.setLawyerId((long) userDTO.getId());
            lawyerDTO.setName(userDTO.getName());
            lawyerDTO.setEmail(userDTO.getEmail());
            lawyerDTO.setPhone(userDTO.getPhone());
            lawyerDTO.setSpecialty(userDTO.getSpecialty());
            lawyerDTO.setIntroduction(userDTO.getIntroduction());

            System.out.println("LawyerDTO lawyerId: " + lawyerDTO.getLawyerId());
            System.out.println("LawyerDTO specialty: " + lawyerDTO.getSpecialty());

            int result = lawyerDAO.updateLawyerProfile(lawyerDTO);
            System.out.println("updateLawyerProfile result: " + result);
        }
    }

    // 파일 저장 유틸리티
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