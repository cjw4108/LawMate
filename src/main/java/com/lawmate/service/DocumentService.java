package com.lawmate.service;

import com.lawmate.dao.DocumentDAO;
import com.lawmate.dto.DocumentCategoryDTO;
import com.lawmate.dto.DocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    @Autowired
    private DocumentDAO documentDAO;

    public Map<String, Object> getDocuments(int page, int size, Long categoryId, String keyword) {
        List<DocumentDTO> allDocs;
        if (categoryId != null) {
            allDocs = documentDAO.selectDocumentsByCategory(categoryId);
        } else {
            allDocs = documentDAO.selectAllDocuments();
        }

        if (keyword != null && !keyword.isEmpty()) {
            final String kw = keyword.toLowerCase();
            allDocs = allDocs.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(kw))
                    .collect(java.util.stream.Collectors.toList());
        }

        int total = allDocs.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);
        List<DocumentDTO> pageDocs = fromIndex < total ? allDocs.subList(fromIndex, toIndex) : new ArrayList<>();
        List<DocumentCategoryDTO> categories = documentDAO.selectAllCategories();

        Map<String, Object> result = new HashMap<>();
        result.put("documents", pageDocs);
        result.put("categories", categories);
        result.put("total", total);
        result.put("totalPages", Math.max(1, (int) Math.ceil((double) total / size)));
        result.put("currentPage", page);
        return result;
    }

    public List<DocumentDTO> autocomplete(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>();
        return documentDAO.searchByKeyword(keyword);
    }

    public DocumentDTO getDocumentById(Long id) {
        return documentDAO.selectDocumentById(id);
    }
}