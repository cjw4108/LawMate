package com.lawmate.dao;

import com.lawmate.dto.DocumentDTO;
import com.lawmate.dto.DocumentCategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 문서 관리 DAO
 */
@Mapper
public interface DocumentDAO {
    
    // 카테고리 관련
    void insertCategory(DocumentCategoryDTO category);
    List<DocumentCategoryDTO> selectAllCategories();
    DocumentCategoryDTO selectCategoryById(Long id);
    DocumentCategoryDTO selectCategoryByName(String name);
    
    // 문서 관련
    void insertDocument(DocumentDTO document);
    void insertDocumentBatch(List<DocumentDTO> documents);
    List<DocumentDTO> selectAllDocuments();
    List<DocumentDTO> selectDocumentsByCategory(Long categoryId);
    DocumentDTO selectDocumentById(Long id);
    int countDocuments();
    int countDocumentsByCategory(Long categoryId);
    
    // 삭제 (테스트용)
    void deleteAllDocuments();
    void deleteAllCategories();
}
