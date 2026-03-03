package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List; // ★ List를 쓰기 위해 이 줄이 꼭 있어야 합니다!

@Mapper
public interface UserDAO {
    // --- 기존 팀원들 메서드 (그대로 유지) ---
    UserDTO login(@Param("userId") String userId, @Param("password") String password);
    void signup(UserDTO userDTO);
    int saveLawyer(UserDTO userDTO);
    int existsByUserId(String userId);
    UserDTO findByUserId(String userId);
    int updateProfile(UserDTO userDTO);

    // --- 본인 추가 메서드 (관리자용) ---

    // 1. 승인 대기 중인 변호사 목록 조회 (추가된 부분)
    List<UserDTO> findPendingLawyers();

    // 2. 변호사 승인 상태 업데이트 (기존에 있던 것 활용)
    int updateLawyerStatus(@Param("userId") String userId,
                           @Param("lawyerStatus") String lawyerStatus,
                           @Param("rejectReason") String rejectReason);
}