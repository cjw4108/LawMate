package com.lawmate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawmate.dao.DocumentDAO;
import com.lawmate.dto.DocumentCategoryDTO;
import com.lawmate.dto.DocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DocumentLoaderService {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoaderService.class);

    private final DocumentDAO documentDAO;
    private final ObjectMapper objectMapper;

    private final Map<String, Long> categoryMapping = new HashMap<>();

    public DocumentLoaderService(DocumentDAO documentDAO, ObjectMapper objectMapper) {
        this.documentDAO = documentDAO;
        this.objectMapper = objectMapper;
    }

    // [기존 팀원 코드] 법률 양식 데이터 초기 로드 로직
    public void loadLegalForms() throws IOException {
        log.info("=".repeat(60));
        log.info("법률 양식 데이터 로드 시작");
        log.info("=".repeat(60));
        createCategories();
        List<Map<String, String>> jsonData = readJsonFile();
        List<DocumentDTO> documents = convertToDocuments(jsonData);
        log.info("DB 저장 중...");
        for (DocumentDTO doc : documents) {
            documentDAO.insertDocument(doc);
        }
        log.info("  ✓ {}개 문서 저장 완료", documents.size());
        printStatistics();
        log.info("=".repeat(60));
        log.info("법률 양식 데이터 로드 완료!");
        log.info("=".repeat(60));
    }

    private void createCategories() {
        log.info("카테고리 생성 중...");
        try {
            List<DocumentCategoryDTO> existing = documentDAO.selectAllCategories();
            if (!existing.isEmpty()) {
                log.info("기존 카테고리 사용 ({}개)", existing.size());
                for (DocumentCategoryDTO category : existing) {
                    categoryMapping.put(category.getName(), category.getId());
                }
                return;
            }
            String[] categories = {"부동산", "민사", "형사", "이혼/가족", "노동", "기타"};
            String[] descriptions = {
                    "부동산 관련 법률 서식", "민사 관련 법률 서식", "형사 관련 법률 서식",
                    "이혼 및 가족 관련 법률 서식", "노동 관련 법률 서식", "기타 법률 서식"
            };
            for (int i = 0; i < categories.length; i++) {
                DocumentCategoryDTO category = new DocumentCategoryDTO();
                category.setName(categories[i]);
                category.setDescription(descriptions[i]);
                category.setDisplayOrder(i + 1);
                category.setIsActive(1);
                documentDAO.insertCategory(category);
                DocumentCategoryDTO created = documentDAO.selectCategoryByName(categories[i]);
                categoryMapping.put(created.getName(), created.getId());
                log.info("  ✓ 카테고리 생성: {} (ID: {})", created.getName(), created.getId());
            }
        } catch (Exception e) {
            log.error("에러 발생!", e);
            throw e;
        }
    }

    private List<Map<String, String>> readJsonFile() throws IOException {
        log.info("JSON 파일 읽는 중...");
        ClassPathResource resource = new ClassPathResource("legal-forms/legal_forms.json");
        List<Map<String, String>> data = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<Map<String, String>>>() {}
        );
        log.info("  ✓ {}개 항목 발견", data.size());
        return data;
    }

    private List<DocumentDTO> convertToDocuments(List<Map<String, String>> jsonData) {
        log.info("DTO 변환 중...");
        List<DocumentDTO> documents = new ArrayList<>();
        for (Map<String, String> item : jsonData) {
            String category = item.get("category");
            String title = item.get("title");
            String subcategory = item.get("subcategory");
            String filePath = item.get("file_path");
            Long categoryId = categoryMapping.get(category);
            if (categoryId == null) {
                log.warn("  ⚠ 알 수 없는 카테고리: {}", category);
                continue;
            }
            DocumentDTO doc = new DocumentDTO(categoryId, title, subcategory, filePath, "HWP");
            documents.add(doc);
        }
        log.info("  ✓ {}개 문서 변환 완료", documents.size());
        return documents;
    }

    private void printStatistics() {
        log.info("\n통계:");
        log.info("  - 전체 문서: {}개", documentDAO.countDocuments());
        List<DocumentCategoryDTO> categories = documentDAO.selectAllCategories();
        for (DocumentCategoryDTO category : categories) {
            int count = documentDAO.countDocumentsByCategory(category.getId());
            log.info("  - {}: {}개", category.getName(), count);
        }
    }

    public void clearAllData() {
        log.warn("모든 데이터 삭제 중...");
        documentDAO.deleteAllDocuments();
        documentDAO.deleteAllCategories();
        categoryMapping.clear();
        log.warn("삭제 완료");
    }

    // ==========================================
    // [내 기능] 마이페이지 전용 추가 메서드
    // ==========================================

    /**
     * 회원별 문서 이력 조회 (이미지 d7253f 컨트롤러 연동)
     */
    public List<DocumentDTO> getUserDownloadList(String userId) {
        log.info("사용자 [{}]의 문서 다운로드 이력 조회 시작", userId);
        return documentDAO.selectDocumentsByUserId(userId);
    }

    /**
     * 문서 파일 경로 조회 및 다운로드 이력 기록
     */
    public String getFilePath(Long documentId, String userId) {
        // 1. 파일 경로 조회 (팀원이 만든 메서드 활용 가능 여부 확인 후 호출)
        String filePath = documentDAO.selectFilePathById(documentId);

        if (filePath != null) {
            // 2. 다운로드 이력 저장 (본인 전용 쿼리 호출)
            documentDAO.insertDownloadHistory(userId, documentId);
            log.info("사용자 [{}]의 문서(ID: {}) 다운로드 이력 기록 완료", userId, documentId);
        }

        return filePath;
    }
}