package com.lawmate.dao;

import com.lawmate.dto.LawyerApprovalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminLawyerDAO {

    // ✅ [핵심 수정] role, status 파라미터를 추가하여 필터링이 가능하게 합니다.
    // @Param 어노테이션의 이름이 XML의 #{name}과 일치해야 합니다.
    List<LawyerApprovalDTO> findPendingLawyers(@Param("role") String role,
                                               @Param("status") String status,
                                               @Param("keyword") String keyword);

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

    List<LawyerApprovalDTO> findPendingLawyers(String keyword);
}
