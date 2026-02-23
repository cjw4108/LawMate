package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 추가됨
import java.util.List;

@Mapper
public interface UserDAO {
    // XML의 #{userId}와 #{password}가 이 @Param 이름과 연결됩니다.
    UserDTO login(@Param("userId") String userId, @Param("password") String password);

    // 가입 성공 시 1, 실패 시 0을 반환하도록 int 타입을 권장합니다.
    int save(UserDTO user);

    // 아이디 중복 확인 시 사용 (1이면 이미 있음)
    int existsByUserId(String userId);
}