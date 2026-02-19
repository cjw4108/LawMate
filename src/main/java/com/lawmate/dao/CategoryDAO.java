package com.lawmate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lawmate.dto.CategoryDTO;

@Mapper
public interface CategoryDAO {

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
    List<CategoryDTO> categorySch(CategoryDTO sch);

    @Select("""
        SELECT
            category_id,
            name,
            description,
            created_at
        FROM LAW_CATEGORY
        WHERE category_id = #{categoryId}
    """)
    CategoryDTO getCategoryById(int categoryId);

    // 카테고리별 총 조회수 조회 추가
    @Select("""
        SELECT
            C.category_id,
            C.name,
            C.description,
            C.created_at,
            NVL(SUM(L.view_count), 0) AS total_view_count
        FROM LAW_CATEGORY C
        LEFT JOIN LAW_CONTENT L ON C.category_id = L.category_id
        GROUP BY C.category_id, C.name, C.description, C.created_at
    """)
    List<CategoryDTO> getCategoryViewCount();
}