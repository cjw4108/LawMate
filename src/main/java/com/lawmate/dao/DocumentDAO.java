package com.lawmate.dao;

import com.lawmate.dto.DocumentDTO;
import com.lawmate.dto.DocumentCategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocumentDAO {

    void insertCategory(DocumentCategoryDTO category);
    List<DocumentCategoryDTO> selectAllCategories();
    DocumentCategoryDTO selectCategoryById(Long id);
    DocumentCategoryDTO selectCategoryByName(String name);

    void insertDocument(DocumentDTO document);
    void insertDocumentBatch(List<DocumentDTO> documents);
    List<DocumentDTO> selectAllDocuments();
    List<DocumentDTO> selectDocumentsByCategory(Long categoryId);
    DocumentDTO selectDocumentById(Long id);
    int countDocuments();
    int countDocumentsByCategory(Long categoryId);

    void deleteAllDocuments();
    void deleteAllCategories();
}