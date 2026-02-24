package com.lawmate.dao;

import com.lawmate.dto.AdminDTO;
import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminDAO {

    // 1. 기존 관리자 로그인 기능
    AdminDTO login(@Param("adminId") String adminId, @Param("adminPw") String adminPw);

    // 2. 변호사 승인 대기 목록 조회
    List<UserDTO> selectPendingLawyers(@Param("role") String role,
                                       @Param("status") String status,
                                       @Param("keyword") String keyword);

    // 3. 변호사 승인/반려 상태 업데이트
    int updateLawyerStatus(@Param("userId") String userId,
                           @Param("status") String status,
                           @Param("rejectReason") String rejectReason);
}