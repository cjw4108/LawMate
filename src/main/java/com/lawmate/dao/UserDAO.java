package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserDAO {

    /* 로그인 */
    UserDTO login(String userId, String password);

    /* 회원가입 */
    boolean save(UserDTO user);

    /* 아이디 중복 체크 */
    int existsByUserId(String userId);

    /* 관리자 – 승인 대기 변호사 조회 */
    List<UserDTO> findPendingLawyers();
}