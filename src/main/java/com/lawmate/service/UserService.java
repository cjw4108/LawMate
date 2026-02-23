package com.lawmate.service;

import com.lawmate.dao.UserDAO;
import com.lawmate.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    /**
     * 회원가입 (일반 / 변호사 공용)
     */
    @Transactional // 데이터 저장 중 에러 발생 시 롤백을 위해 추가
    public boolean signup(UserDTO user) {

        // 1. 아이디 중복 체크 (기존 DAO 활용)
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        // 2. 권한(Role)에 따른 처리
        if ("USER".equals(user.getRole())) {
            user.setLawyerStatus(null);
            user.setLicenseFile(null);
        } else if ("LAWYER".equals(user.getRole())) {
            user.setLawyerStatus("PENDING"); // 변호사는 승인 대기 상태로 시작
        } else {
            // Role이 정의되지 않은 경우 기본값 설정 혹은 거절
            user.setRole("USER");
        }

        // 3. DB 저장 (1 이상이면 성공)
        return userDAO.save(user) > 0;
    }

    /**
     * 로그인
     */
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }
}