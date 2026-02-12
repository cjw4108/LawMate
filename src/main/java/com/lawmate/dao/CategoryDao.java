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
    where name like #{name} and description like #{description}
    """)
    List<CategoryDto> categorySch(CategoryDto sch);
}
