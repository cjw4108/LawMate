package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {

    private final List<UserDTO> users = new ArrayList<>();

    public UserDAO() {
        // 관리자 계정
        UserDTO admin = new UserDTO();
        admin.setUserId("admin");
        admin.setPassword("1234");
        admin.setRole("ADMIN");
        users.add(admin);
    }

    /* 로그인 */
    public UserDTO login(String userId, String password) {
        for (UserDTO user : users) {
            if (user.getUserId().equals(userId)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /* 회원가입 */
    public boolean save(UserDTO user) {
        if (existsByUserId(user.getUserId())) {
            return false;
        }
        users.add(user);
        return true;
    }

    /* 아이디 중복 체크 */
    public boolean existsByUserId(String userId) {
        return users.stream()
                .anyMatch(u -> u.getUserId().equals(userId));
    }

    /* 관리자 – 승인 대기 변호사 조회 */
    public List<UserDTO> findPendingLawyers() {
        List<UserDTO> result = new ArrayList<>();
        for (UserDTO user : users) {
            if ("LAWYER".equals(user.getRole())
                    && "PENDING".equals(user.getLawyerStatus())) {
                result.add(user);
            }
        }
        return result;
    }
}