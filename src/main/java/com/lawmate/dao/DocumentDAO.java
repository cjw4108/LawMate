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

    // ── 카테고리 관련 (팀원 공통) ──
    void insertCategory(DocumentCategoryDTO category);
    List<DocumentCategoryDTO> selectAllCategories();
    DocumentCategoryDTO selectCategoryById(Long id);
    DocumentCategoryDTO selectCategoryByName(String name);

    // ── 문서 관련 (팀원 공통) ──
    void insertDocument(DocumentDTO document);
    void insertDocumentBatch(List<DocumentDTO> documents);
    List<DocumentDTO> selectAllDocuments();
    List<DocumentDTO> selectDocumentsByCategory(Long categoryId);
    DocumentDTO selectDocumentById(Long id);
    int countDocuments();
    int countDocumentsByCategory(Long categoryId);

    // ── 삭제 (테스트용) ──
    void deleteAllDocuments();
    void deleteAllCategories();

    // ── 문서 파일 경로 조회 (다운로드용 공통) ──
    String selectFilePathById(@Param("id") Long id);

    // ── 마이페이지 전용 (본인 추가분) ──

    /**
     * [내 기능] 회원별 문서 다운로드 이력 조회
     * Mapper ID: selectDocumentsByUserIdForMypage
     */
    List<DocumentDTO> selectDocumentsByUserIdForMypage(String userId);

    /**
     * [내 기능] 문서 다운로드 이력 저장 (중복 에러 방지를 위해 ID 변경)
     * Mapper ID: insertDownloadHistoryForMypage
     */
    void insertDownloadHistoryForMypage(@Param("userId") String userId,
                                        @Param("documentId") Long documentId);
}