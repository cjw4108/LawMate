package com.lawmate.controller;

import com.lawmate.dao.DocumentDAO;
import com.lawmate.dto.DocumentCategoryDTO;
import com.lawmate.dto.DocumentDTO;
import com.lawmate.service.DocumentLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DocumentController {

    @Autowired
    private DocumentLoaderService documentLoaderService;

    @Autowired
    private DocumentDAO documentDAO;

    @GetMapping("/docs")
    public String document() {
        return "docs";
    }

    @GetMapping("/api/documents")
    @ResponseBody
    public Map<String, Object> getDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        List<DocumentDTO> allDocs;
        if (categoryId != null) {
            allDocs = documentDAO.selectDocumentsByCategory(categoryId);
        } else {
            allDocs = documentDAO.selectAllDocuments();
        }

        // 검색 필터링
        if (keyword != null && !keyword.isEmpty()) {
            final String kw = keyword.toLowerCase();
            allDocs = allDocs.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(kw))
                    .collect(java.util.stream.Collectors.toList());
        }

        int total = allDocs.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);
        List<DocumentDTO> pageDocs = fromIndex < total ? allDocs.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        List<DocumentCategoryDTO> categories = documentDAO.selectAllCategories();

        Map<String, Object> result = new HashMap<>();
        result.put("documents", pageDocs);
        result.put("categories", categories);
        result.put("total", total);
        result.put("totalPages", Math.max(1, (int) Math.ceil((double) total / size)));
        result.put("currentPage", page);
        return result;
    }

    @GetMapping("/docs/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            DocumentDTO doc = documentDAO.selectDocumentById(id);
            ClassPathResource resource = new ClassPathResource(doc.getFilePath());

            String filename = resource.getFilename();
            String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/load")
    public ResponseEntity<Map<String, Object>> loadDocuments() {
        Map<String, Object> response = new HashMap<>();
        try {
            documentLoaderService.loadLegalForms();
            response.put("success", true);
            response.put("message", "데이터 로드 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        try {
            List<DocumentCategoryDTO> list = documentDAO.selectAllCategories();
            return "카테고리 개수: " + list.size();
        } catch (Exception e) {
            return "에러: " + e.getMessage();
        }
    }
}