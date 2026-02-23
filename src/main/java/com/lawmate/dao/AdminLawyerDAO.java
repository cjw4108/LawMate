package com.lawmate.dao;

import com.lawmate.dto.LawyerApprovalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminLawyerDAO {

    // 승인 대기 목록 조회
    List<LawyerApprovalDTO> findPendingLawyers();

    // 전체 변호사 목록 조회 (상태 필터용)
    List<LawyerApprovalDTO> findAllLawyers();

    // 변호사 상세 조회
    LawyerApprovalDTO findByLawyerId(String lawyerId);

    // 승인 처리
    int approve(String lawyerId);

    // 반려 처리 (반려 사유 포함)
    int reject(@Param("lawyerId") String lawyerId,
               @Param("rejectReason") String rejectReason);

    // 상태 업데이트 (승인/반려 통합)
    int updateStatus(@Param("lawyerId") String lawyerId,
                     @Param("status") String status,
                     @Param("rejectReason") String rejectReason);
}