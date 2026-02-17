package com.lawmate.controller;

import com.lawmate.dao.DocumentDAO;  // 추가!
import com.lawmate.dto.DocumentCategoryDTO;
import com.lawmate.service.DocumentLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;  // 추가!

import java.util.HashMap;
import java.util.List;  // 추가!
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