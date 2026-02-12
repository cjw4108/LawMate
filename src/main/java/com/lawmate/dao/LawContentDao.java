package com.lawmate.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.lawmate.dto.LawContentDto;

@Mapper
public interface LawContentDao {

    @Select("""
        SELECT 
            content_id,
            category_id,
            deep_category,
            title,
            summary,
            view_count,
            created_at,
            updated_at
        FROM LAW_CONTENT
        WHERE category_id = #{categoryId}
        ORDER BY created_at DESC
    """)
    List<LawContentDto> getContentsByCategory(int categoryId);
}