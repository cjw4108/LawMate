package com.lawmate.controller;

import com.lawmate.dto.LegalDicDTO;
import com.lawmate.service.LegalDicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LegalDicController {

    private final LegalDicService legalDicService;

    public LegalDicController(LegalDicService legalDicService) {
        this.legalDicService = legalDicService;
    }

    @GetMapping("/legal-dictionary")
    public String legalDictionary(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "chosung", required = false) String chosung,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        List<LegalDicDTO> fullList;

        // 🔎 검색 / 초성 / 전체 구분
        if (keyword != null && !keyword.trim().isEmpty()) {
            fullList = legalDicService.search(keyword);
            model.addAttribute("keyword", keyword);
            model.addAttribute("mode", "search");

        } else if (chosung != null && !chosung.trim().isEmpty()) {
            fullList = legalDicService.getByChosung(chosung);
            model.addAttribute("chosung", chosung);
            model.addAttribute("mode", "chosung");

        } else {
            fullList = legalDicService.getAll();
            model.addAttribute("mode", "all");
        }

        // 🔥 페이징 처리
        int pageSize = 6;  // 한 페이지에 6개
        int totalCount = fullList.size();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);

        List<LegalDicDTO> termList = fullList.subList(startIndex, endIndex);

        // 📦 model에 담기
        model.addAttribute("termList", termList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);

        return "category/LegalDictionary";
    }
}
