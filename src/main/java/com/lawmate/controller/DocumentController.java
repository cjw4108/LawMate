package com.lawmate.controller;

import com.lawmate.dao.DocumentDAO;
import com.lawmate.dto.DocumentDTO;
import com.lawmate.service.DocumentLoaderService;
import com.lawmate.service.DocumentService;
import jakarta.servlet.http.HttpSession;
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
    private DocumentService documentService;

    @Autowired
    private DocumentLoaderService documentLoaderService;

    @Autowired
    private DocumentDAO documentDAO; // 다운로드에서만 사용

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
        return documentService.getDocuments(page, size, categoryId, keyword);
    }

    // 자동완성 전용 API
    @GetMapping("/api/documents/autocomplete")
    @ResponseBody
    public List<DocumentDTO> autocomplete(@RequestParam String keyword) {
        return documentService.autocomplete(keyword);
    }

    @GetMapping("/docs/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            DocumentDTO doc = documentService.getDocumentById(id);
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
            return "카테고리 개수: " + documentDAO.selectAllCategories().size();
        } catch (Exception e) {
            return "에러: " + e.getMessage();
        }
    }
}