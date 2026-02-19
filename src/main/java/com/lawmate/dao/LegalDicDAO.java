package com.lawmate.dao;

import com.lawmate.dto.LegalDicDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LegalDicDAO {

    /** 전체 조회 */
    @Select("SELECT id, term, law, description FROM legal_terms ORDER BY id")
    @Results(id = "legalTermResultMap", value = {
            @Result(property = "id",          column = "id"),
            @Result(property = "term",        column = "term"),
            @Result(property = "law",         column = "law"),
            @Result(property = "description", column = "description")
    })
    List<LegalDicDTO> selectAll();

    /** 키워드 검색 (용어명, 설명, 관련법) */
    @Select("SELECT id, term, law, description FROM legal_terms " +
            "WHERE term LIKE '%' || #{keyword} || '%' " +
            "   OR description LIKE '%' || #{keyword} || '%' " +
            "   OR law LIKE '%' || #{keyword} || '%' " +
            "ORDER BY id")
    @ResultMap("legalTermResultMap")
    List<LegalDicDTO> selectByKeyword(@Param("keyword") String keyword);

    /** 초성 필터 - 한글 유니코드 범위로 필터링 (Oracle REGEXP_LIKE) */
    @Select("<script>" +
            "SELECT id, term, law, description FROM legal_terms " +
            "WHERE 1=1 " +
            "<if test=\"chosung != null and chosung != ''\">" +
            " AND REGEXP_LIKE(SUBSTR(term,1,1), " +
            "   CASE #{chosung} " +
            "     WHEN 'ㄱ' THEN '^[가-깋]' " +
            "     WHEN 'ㄴ' THEN '^[나-닣]' " +
            "     WHEN 'ㄷ' THEN '^[다-딯]' " +
            "     WHEN 'ㄹ' THEN '^[라-맇]' " +
            "     WHEN 'ㅁ' THEN '^[마-밓]' " +
            "     WHEN 'ㅂ' THEN '^[바-빟]' " +
            "     WHEN 'ㅅ' THEN '^[사-싷]' " +
            "     WHEN 'ㅇ' THEN '^[아-잏]' " +
            "     WHEN 'ㅈ' THEN '^[자-짛]' " +
            "     WHEN 'ㅊ' THEN '^[차-칳]' " +
            "     WHEN 'ㅋ' THEN '^[카-킿]' " +
            "     WHEN 'ㅌ' THEN '^[타-팋]' " +
            "     WHEN 'ㅍ' THEN '^[파-핗]' " +
            "     WHEN 'ㅎ' THEN '^[하-힣]' " +
            "   END)" +
            "</if>" +
            " ORDER BY id" +
            "</script>")
    @ResultMap("legalTermResultMap")
    List<LegalDicDTO> selectByChosung(@Param("chosung") String chosung);

    /** 단건 조회 */
    @Select("SELECT id, term, law, description FROM legal_terms WHERE id = #{id}")
    @ResultMap("legalTermResultMap")
    LegalDicDTO selectById(@Param("id") Long id);
}