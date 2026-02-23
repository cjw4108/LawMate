package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDAO {
    // 로그인 체크
    UserDTO login(@Param("userId") String userId, @Param("password") String password);

    // 실제 DB에 데이터 삽입 (회원가입)
    int save(UserDTO user);

    // 중복 확인
    int existsByUserId(String userId);
}