package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    /**
     * 회원가입 (일반 / 변호사 공용)
     */
    public boolean signup(UserDTO user) {

        // 일반 회원
        if ("USER".equals(user.getRole())) {
            user.setLawyerStatus(null);
            user.setLicenseFile(null);
            return userDAO.save(user);
        }

        // 변호사 회원
        if ("LAWYER".equals(user.getRole())) {
            user.setLawyerStatus("PENDING"); // 관리자 승인 대기
            return userDAO.save(user);
        }

        return false;
    }

    /**
     * 로그인
     */
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }
}