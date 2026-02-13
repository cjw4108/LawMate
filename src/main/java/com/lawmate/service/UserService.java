package com.lawmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }
}