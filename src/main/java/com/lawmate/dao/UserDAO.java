package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {

    // 임시 메모리 저장소 (나중에 DB로 교체)
    private final List<UserDTO> users = new ArrayList<>();

    public UserDAO() {
        // 관리자
        UserDTO admin = new UserDTO("admin", "1234", "admin@lawmate.com", "ADMIN");
        users.add(admin);

        // 승인된 변호사
        UserDTO lawyer = new UserDTO("lawyer01", "1234", "lawyer@lawmate.com", "LAWYER");
        lawyer.setLawyerStatus("APPROVED");
        users.add(lawyer);

        // 일반 회원
        UserDTO user = new UserDTO("user01", "1234", "user@lawmate.com", "USER");
        users.add(user);
    }

    /* ================= 로그인 ================= */
    public UserDTO login(String userId, String password) {
        for (UserDTO user : users) {
            if (user.getUserId().equals(userId)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /* ================= 회원가입 ================= */
    public boolean save(UserDTO user) {
        if (existsByUserId(user.getUserId())) {
            return false;
        }
        users.add(user);
        return true;
    }

    /* ================= 아이디 중복 체크 ================= */
    public boolean existsByUserId(String userId) {
        for (UserDTO user : users) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}