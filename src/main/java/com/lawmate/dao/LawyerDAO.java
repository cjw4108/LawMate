package com.lawmate.dao;

import com.lawmate.dto.LawyerDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 변호사 MyBatis Mapper 인터페이스
 */
@Mapper
public interface LawyerDAO {

    /** 변호사 목록 조회 (페이징) */
    List<LawyerDTO> selectLawyerList(LawyerDTO dto);
    int updateLawyerProfile(LawyerDTO lawyerDTO);
    /** 전체 건수 */
    int selectLawyerCount(LawyerDTO dto);

    /** 변호사 단건 조회 */
    LawyerDTO selectLawyerDetail(Long lawyerId);

    /** 변호사 이메일로 단건 조회 (추가) */
    LawyerDTO selectLawyerByEmail(String email);  // ← 이것만 추가

    /** 변호사 등록 */
    int insertLawyer(LawyerDTO dto);

    /** 변호사 수정 */
    int updateLawyer(LawyerDTO dto);

    /** 변호사 삭제 (논리삭제) */
    int deleteLawyer(Long lawyerId);
}
