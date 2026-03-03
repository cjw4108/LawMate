package com.lawmate.service;

import org.springframework.stereotype.Service; // 이 줄이 반드시 있어야 합니다.
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.lawmate.dao.AdminUserRepository;
import com.lawmate.entity.UserEntity;
import java.util.List;

@Service
@Transactional
public class AdminUserService {
    @Autowired private AdminUserRepository userRepository;

    public List<UserEntity> getUsers(String filter, String keyword) {
        // 'active' -> '정상', 'banned' -> '정지'로 매핑하여 조회
        String statusFilter = filter.equals("active") ? "정상" : filter.equals("banned") ? "정지" : "all";
        return userRepository.findUsersByFilter(statusFilter, keyword);
    }

    public void changeStatus(String userId, String newStatus) {
        userRepository.updateUserStatus(userId, newStatus);
    }
}