package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public boolean signup(UserDTO user) {
        return userDAO.save(user);
    }
}