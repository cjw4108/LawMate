package com.lawmate.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.lawmate.dto.LawContentDTO;

@Mapper
public interface LawContentDAO {

    // 카테고리별 법률정보 목록 조회
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
    List<LawContentDTO> getContentsByCategory(int categoryId);

    // 법률정보 상세 조회 (카테고리명 포함, CLOB 필드 포함)
    @Select("""
        SELECT 
            c.content_id,
            c.category_id,
            c.deep_category,
            c.title,
            c.summary,
            c.content,
            c.process,
            c.documents,
            c.view_count,
            c.created_at,
            c.updated_at,
            cat.name as categoryName
        FROM LAW_CONTENT c
        LEFT JOIN LAW_CATEGORY cat ON c.category_id = cat.category_id
        WHERE c.content_id = #{contentId}
    """)
    LawContentDTO getContentById(int contentId);

    // 조회수 증가
    @Update("""
        UPDATE LAW_CONTENT
        SET view_count = view_count + 1
        WHERE content_id = #{contentId}
    """)
    void increaseViewCount(int contentId);
}