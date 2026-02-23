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
    @Transactional
    public boolean signup(UserDTO user) {

        // 1. 아이디 중복 체크
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        // 2. 비밀번호 일치 확인 (DTO에 추가한 passwordConfirm 활용)
        if (user.getPasswordConfirm() != null && !user.getPassword().equals(user.getPasswordConfirm())) {
            return false;
        }

        // 3. 권한(Role)에 따른 처리
        if ("USER".equals(user.getRole())) {
            user.setLawyerStatus(null);
            user.setLicenseFile(null);
        } else if ("LAWYER".equals(user.getRole())) {
            // 변호사는 기본적으로 승인 대기 상태로 시작
            if (user.getLawyerStatus() == null) {
                user.setLawyerStatus("PENDING");
            }
        } else {
            user.setRole("USER");
        }

        // 4. DB 저장
        return userDAO.save(user) > 0;
    }

    /**
     * 로그인 처리
     */
    public UserDTO login(String userId, String password) {
        // DAO에서 아이디와 비밀번호로 사용자 조회
        UserDTO user = userDAO.login(userId, password);

        // 추가 로직: 로그인이 성공했더라도 탈퇴 회원이나 차단 회원을 걸러낼 수 있음
        return user;
    }
}