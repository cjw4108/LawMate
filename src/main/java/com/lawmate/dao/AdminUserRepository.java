package com.lawmate.dao;

import com.lawmate.entity.UserEntity; // 실제 사용하는 User 엔티티
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminUserRepository extends JpaRepository<UserEntity, String> {

    // 필터 및 아이디 검색
    @Query("SELECT u FROM UserEntity u WHERE " +
            "(:filter = 'all' OR u.status = :filter) AND " +
            "(:keyword IS NULL OR u.userId LIKE %:keyword%)")
    List<UserEntity> findUsersByFilter(@Param("filter") String filter, @Param("keyword") String keyword);

    // 상태 변경 (정상 <-> 정지)
    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status WHERE u.userId = :userId")
    void updateUserStatus(@Param("userId") String userId, @Param("status") String status);
}