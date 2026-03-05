package com.lawmate.dao;

import com.lawmate.dto.LawyerApprovalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminLawyerDAO {

    // ✅ 검색 기능 추가: keyword 파라미터를 받도록 수정
    List<LawyerApprovalDTO> findPendingLawyers(@Param("keyword") String keyword);

    // 전체 변호사 목록 조회
    List<LawyerApprovalDTO> findAllLawyers();

    // 변호사 상세 조회
    LawyerApprovalDTO findByLawyerId(String lawyerId);

    // 승인 처리
    int approve(String lawyerId);

    // 반려 처리
    int reject(@Param("lawyerId") String lawyerId,
               @Param("rejectReason") String rejectReason);

    // 상태 업데이트
    int updateStatus(@Param("lawyerId") String lawyerId,
                     @Param("status") String status,
                     @Param("rejectReason") String rejectReason);
}