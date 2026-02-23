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

    // 1. 일반 회원가입 (기존 로직 그대로 유지)
    @Transactional
    public boolean signup(UserDTO user) {
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        user.setRole("ROLE_USER");
        user.setLawyerStatus("NONE");

        userDAO.signup(user);
        return true;
    }

    // 2. 변호사 회원가입 (에러 방지를 위해 수정된 최종 로직)
    @Transactional
    public boolean signupLawyer(UserDTO user, MultipartFile licenseFile) {
        if (licenseFile != null && !licenseFile.isEmpty()) {
            try {
                // 파일은 지정된 경로(C:/lawmate/uploads/)에 물리적으로 저장
                String savedName = saveFile(licenseFile);

                // 🔴 [에러 해결 핵심] DB에 LICENSE_FILE 컬럼이 없으므로 DTO에 세팅하지 않음.
                // 이렇게 해야 MyBatis가 존재하지 않는 컬럼에 데이터를 넣으려다 에러(ORA-00904)를 내지 않습니다.
                // user.setLicenseFile(savedName);

                System.out.println("변호사 증빙파일 저장 완료: " + savedName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // XML에서 LICENSE_FILE 항목이 제거된 saveLawyer 쿼리를 호출
        return userDAO.saveLawyer(user) > 0;
    }

    // 3. 로그인 및 기타 기능 (기존 유지)
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