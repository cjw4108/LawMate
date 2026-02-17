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

    // 카테고리 매핑
    private final Map<String, Long> categoryMapping = new HashMap<>();

    // 생성자
    public DocumentLoaderService(DocumentDAO documentDAO, ObjectMapper objectMapper) {
        this.documentDAO = documentDAO;
        this.objectMapper = objectMapper;
    }

    /**
     * 법률 양식 데이터 로드
     */
    public void loadLegalForms() throws IOException {
        log.info("=".repeat(60));
        log.info("법률 양식 데이터 로드 시작");
        log.info("=".repeat(60));

        // 1단계: 카테고리 생성
        createCategories();

        // 2단계: JSON 파일 읽기
        List<Map<String, String>> jsonData = readJsonFile();

        // 3단계: DTO 변환
        List<DocumentDTO> documents = convertToDocuments(jsonData);

        // 4단계: DB 저장 (배치)
        documentDAO.insertDocumentBatch(documents);

        // 5단계: 결과 출력
        printStatistics();

        log.info("=".repeat(60));
        log.info("법률 양식 데이터 로드 완료!");
        log.info("=".repeat(60));
    }

    /**
     * 카테고리 생성
     */
    private void createCategories() {
        log.info("카테고리 생성 중...");

        try {
            // 이미 있는지 확인
            log.info("selectAllCategories 호출 전...");
            List<DocumentCategoryDTO> existing = documentDAO.selectAllCategories();
            log.info("selectAllCategories 호출 후! 개수: {}", existing.size());

            if (!existing.isEmpty()) {
                log.info("기존 카테고리 사용 ({}개)", existing.size());
                for (DocumentCategoryDTO category : existing) {
                    categoryMapping.put(category.getName(), category.getId());
                }
                return;
            }

            // 카테고리 생성
            String[] categories = {"부동산", "민사", "형사", "이혼/가족", "노동", "기타"};
            String[] descriptions = {
                    "부동산 관련 법률 서식",
                    "민사 관련 법률 서식",
                    "형사 관련 법률 서식",
                    "이혼 및 가족 관련 법률 서식",
                    "노동 관련 법률 서식",
                    "기타 법률 서식"
            };

            for (int i = 0; i < categories.length; i++) {
                DocumentCategoryDTO category = new DocumentCategoryDTO();
                category.setName(categories[i]);
                category.setDescription(descriptions[i]);
                category.setDisplayOrder(i + 1);
                category.setIsActive(1);

                documentDAO.insertCategory(category);

                // 생성된 카테고리 조회
                DocumentCategoryDTO created = documentDAO.selectCategoryByName(categories[i]);
                categoryMapping.put(created.getName(), created.getId());

                log.info("  ✓ 카테고리 생성: {} (ID: {})", created.getName(), created.getId());
            }
        } catch (Exception e) {
            log.error("에러 발생!", e);
            throw e;
        }
    }

    /**
     * JSON 파일 읽기
     */
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

    /**
     * JSON 데이터를 DTO로 변환
     */
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

            DocumentDTO doc = new DocumentDTO(
                    categoryId,
                    title,
                    subcategory,
                    filePath,
                    "HWP"
            );

            documents.add(doc);
        }

        log.info("  ✓ {}개 문서 변환 완료", documents.size());
        return documents;
    }

    /**
     * 통계 출력
     */
    private void printStatistics() {
        log.info("\n통계:");
        log.info("  - 전체 문서: {}개", documentDAO.countDocuments());

        List<DocumentCategoryDTO> categories = documentDAO.selectAllCategories();
        for (DocumentCategoryDTO category : categories) {
            int count = documentDAO.countDocumentsByCategory(category.getId());
            log.info("  - {}: {}개", category.getName(), count);
        }
    }

    /**
     * 데이터 초기화 (테스트용)
     */
    public void clearAllData() {
        log.warn("모든 데이터 삭제 중...");
        documentDAO.deleteAllDocuments();
        documentDAO.deleteAllCategories();
        categoryMapping.clear();
        log.warn("삭제 완료");
    }
}