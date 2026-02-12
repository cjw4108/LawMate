package com.lawmate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lawmate.dto.CategoryDto;

@Mapper

public interface CategoryDao {

    @Select("""
    SELECT
        category_id,
        name,
        description,
        created_at
    FROM LAW_CATEGORY
    """)
    List<CategoryDto> category();
}
