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
        WHERE name LIKE #{name} 
          AND description LIKE #{description}
    """)
    List<CategoryDto> categorySch(CategoryDto sch);

    // categoryId로 정확히 조회하는 메서드 추가
    @Select("""
        SELECT
            category_id,
            name,
            description,
            created_at
        FROM LAW_CATEGORY
        WHERE category_id = #{categoryId}
    """)
    CategoryDto getCategoryById(int categoryId);
}