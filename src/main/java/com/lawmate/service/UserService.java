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
     * 회원가입 로직: 권한별 상태 설정 및 유효성 검증
     */
    @Transactional
    public boolean signup(UserDTO user) {

        // 1. 아이디 중복 확인
        if (userDAO.existsByUserId(user.getUserId()) > 0) {
            return false;
        }

        // 2. 비밀번호 확인 일치 여부 검증
        if (user.getPasswordConfirm() != null && !user.getPassword().equals(user.getPasswordConfirm())) {
            return false;
        }

        // 3. 역할(Role)에 따른 권한 및 상태값 제어
        if ("LAWYER".equals(user.getRole())) {
            // 변호사는 증빙 서류(자격증 파일)가 없으면 가입 불가
            if (user.getLicenseFile() == null || user.getLicenseFile().isEmpty()) {
                return false;
            }
            // 변호사 가입 시 기본 상태는 '승인 대기(PENDING)'
            user.setLawyerStatus("PENDING");
        } else if ("USER".equals(user.getRole())) {
            // 일반 유저는 별도의 승인 절차 없이 null 처리
            user.setLawyerStatus(null);
            user.setLicenseFile(null);
        } else {
            // 정의되지 않은 권한은 기본 유저로 할당
            user.setRole("USER");
        }

        // 4. DB 저장 수행
        return userDAO.save(user) > 0;
    }

    /**
     * 로그인 로직: 사용자 인증
     */
    public UserDTO login(String userId, String password) {
        return userDAO.login(userId, password);
    }
}