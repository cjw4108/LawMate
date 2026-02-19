package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /* ================= 로그인 ================= */
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }

    /* ================= 회원가입 ================= */
    public boolean register(UserDTO user) {
        // 아이디 중복 체크
        if (userDAO.existsByUserId(user.getUserId())) {
            return false;
        }

        // 저장
        return userDAO.save(user);
    }
}