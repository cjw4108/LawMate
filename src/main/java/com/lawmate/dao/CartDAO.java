package com.lawmate.dao;

import com.lawmate.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDAO {

    // 장바구니 추가
    void insertCart(CartDTO cart);

    // 사용자별 장바구니 조회 (문서 정보 포함)
    List<CartDTO> selectCartByUserId(String userId);

    // 장바구니 개수 조회
    int countCartByUserId(String userId);

    // 장바구니 삭제 (개별)
    void deleteCart(@Param("id") Long id, @Param("userId") String userId);

    // 장바구니 전체 삭제
    void deleteAllCart(String userId);

    // 중복 체크
    int checkDuplicate(@Param("userId") String userId, @Param("documentId") Long documentId);
}