package com.lawmate.dao;

import org.springframework.stereotype.Repository;
import com.lawmate.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {


    private List<UserDTO> users = new ArrayList<>();

    public UserDAO() {
        // 샘플 계정
        users.add(new UserDTO("admin", "1234", "관리자", "ADMIN"));
        users.add(new UserDTO("lawyer01", "1234", "변호사1", "LAWYER"));
        users.add(new UserDTO("user01", "1234", "사용자1", "USER"));
    }

    // 로그인 처리
    public UserDTO login(String userId, String password) {
        for(UserDTO user : users) {
            if(user.getUserId().equals(userId) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}