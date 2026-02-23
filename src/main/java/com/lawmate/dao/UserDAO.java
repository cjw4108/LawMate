package com.lawmate.dao;

import com.lawmate.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDAO {
    // 쿼리는 다 XML에 있으니 여기선 선언만 합니다!
    UserDTO login(@Param("userId") String userId, @Param("password") String password);
    void signup(UserDTO userDTO);
    int saveLawyer(UserDTO userDTO);
    int existsByUserId(String userId);
    UserDTO findByUserId(String userId);
    int updateProfile(UserDTO userDTO);

    int updateLawyerStatus(@Param("userId") String userId,
                           @Param("lawyerStatus") String lawyerStatus,
                           @Param("rejectReason") String rejectReason);
}