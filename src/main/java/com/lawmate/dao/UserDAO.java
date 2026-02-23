package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 추가됨
import java.util.List;

@Mapper
public interface UserDAO {

    /* 로그인 - @Param 추가로 XML과 변수명을 연결 */
    UserDTO login(@Param("userId") String userId, @Param("password") String password);

    /* 회원가입 */
    int save(UserDTO user); // 반환 타입을 int(영향받은 행 수)로 변경하는 것이 관례입니다.

    /* 아이디 중복 체크 */
    int existsByUserId(String userId);

    /* 관리자 – 승인 대기 변호사 조회 */
    List<UserDTO> findPendingLawyers();
}