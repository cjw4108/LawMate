package com.lawmate.dao;

import com.lawmate.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDAO {

    void insertCart(CartDTO cart);

    List<CartDTO> selectCartByUserId(String userId);

    int countCartByUserId(String userId);

    void deleteCart(@Param("id") Long id, @Param("userId") String userId);

    void deleteAllCart(String userId);

    int checkDuplicate(@Param("userId") String userId, @Param("documentId") Long documentId);
}