package com.lawmate.dao;

import com.lawmate.dto.DocumentDTO;
import com.lawmate.dto.DocumentCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    // ====== 마이페이지 - 회원별 문서 이력 ======

    // 회원별 다운로드 문서 이력 조회
    List<DocumentDTO> selectDocumentsByUserId(String userId);

    // 문서 다운로드 이력 저장
    void insertDownloadHistory(@Param("userId") String userId,
                               @Param("documentId") Long documentId);

    // 문서 파일 경로 조회 (다운로드용)
    String selectFilePathById(@Param("documentId") Long documentId,
                              @Param("userId") String userId);

    String selectFilePathById(Long documentId);
}
