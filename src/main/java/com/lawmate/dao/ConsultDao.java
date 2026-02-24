package com.lawmate.dao;

import com.lawmate.dto.ConsultDto;
import com.lawmate.dto.ConsultSchDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsultDao {

    @Select("SELECT * FROM QUESTIONS WHERE TITLE LIKE #{schTitle} ORDER BY ID DESC")
    List<ConsultDto> getConsultList(ConsultSchDto sch);

    @Insert("INSERT INTO QUESTIONS (USER_ID, TITLE, CONTENT, ANSWERED, ADOPTED_ANSWER, CREATED_AT) " +
            "VALUES(#{userId}, #{title}, #{content}, #{answered}, #{adoptedAnswer}, SYSDATE)")
    int insertConsult(ConsultDto ins);

    @Select("SELECT * FROM QUESTIONS WHERE ID = #{id}")
    ConsultDto getConsult(@Param("id") int id);

    @Update("UPDATE QUESTIONS " +
            "SET USER_ID = #{userId}, TITLE = #{title}, CONTENT = #{content}, " +
            "ANSWERED = #{answered}, ADOPTED_ANSWER = #{adoptedAnswer}, CREATED_AT = #{createdAt} " +
            "WHERE ID = #{id}")
    int updateConsult(ConsultDto upt);

    @Delete("DELETE FROM QUESTIONS WHERE ID = #{id}")
    int deleteConsult(@Param("id") int id);

    // ====== 마이페이지 - 일반회원 상담 내역 ======

    // 일반회원 상담 내역 조회
    @Select("SELECT * FROM QUESTIONS WHERE USER_ID = #{userId} ORDER BY CREATED_AT DESC")
    List<ConsultDto> getConsultListByUserId(@Param("userId") String userId);

    // ====== 마이페이지 - 변호사 담당 상담 목록 ======

    // 변호사 담당 상담 목록 조회
    @Select("SELECT * FROM QUESTIONS WHERE LAWYER_ID = #{lawyerId} ORDER BY CREATED_AT DESC")
    List<ConsultDto> getConsultListByLawyerId(@Param("lawyerId") String lawyerId);

    // 상담 상태 업데이트 (진행중, 완료 등)
    @Update("UPDATE QUESTIONS SET STATUS = #{status} WHERE ID = #{id}")
    int updateConsultStatus(@Param("id") int id, @Param("status") String status);
    // [내 부분] 마이페이지 대시보드용 상담 건수 조회
    int getConsultCountByUserId(@Param("userId") String userId);
}

