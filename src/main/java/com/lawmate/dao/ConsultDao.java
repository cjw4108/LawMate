package com.lawmate.dao;

import com.lawmate.dto.ConsultDto;
import com.lawmate.dto.ConsultSchDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsultDao {
    @Select("SELECT * FROM QUESTIONS WHERE TITLE LIKE #{schTitle}\r\n"
            + " ORDER BY ID DESC")
    List<ConsultDto> getConsultList(ConsultSchDto sch);

    @Insert("INSERT INTO QUESTIONS (USER_ID, TITLE, CONTENT, ANSWERED, ADOPTED_ANSWER, CREATED_AT)\r\n"
            + " VALUES(#{userId},#{title},\r\n"
            + "#{content},#{answered},#{adoptedAnswer},SYSDATE)")
    int insertConsult(ConsultDto ins);

    @Select("SELECT * FROM QUESTIONS WHERE ID = #{id}")
    ConsultDto getConsult(@Param("id") int id);

    @Update("UPDATE QUESTIONS\r\n"
            + "	SET USER_ID = #{userId}, TITLE = #{title}, CONTENT = #{content},\r\n"
            + "		ANSWERED = #{answered}, ADOPTED_ANSWER = #{adoptedAnswer}, CREATED_AT = #{createdAt}\r\n"
            + "WHERE ID = #{id}")
    int updateConsult(ConsultDto upt);

    @Delete("DELETE FROM QUESTIONS WHERE ID = #{id}")
    int deleteConsult(@Param("id") int id);
}
