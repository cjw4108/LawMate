package com.lawmate.controller;

import com.lawmate.dto.CategoryDTO;
import com.lawmate.dto.LawContentDTO;
import com.lawmate.service.CategoryService;
import com.lawmate.service.LawContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LawContentService lawContentService;

    @GetMapping("/category")
    public String category(Model model) {
        List<CategoryDTO> list = categoryService.getCategoryViewCount();
        model.addAttribute("categoryList", list);
        return "category/category";
    }

    @GetMapping("/categorylist")
    public String categorylist(
            @RequestParam("categoryId") int categoryId,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        int pageSize = 4; // 한 페이지에 보여줄 글 수

        // 1. 카테고리 조회
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);

        // 2. 해당 카테고리 글 전체 조회
        List<LawContentDTO> allContents = lawContentService.getContentsByCategory(categoryId);

        // 3. 총 글 개수 계산
        int totalContents = allContents.size();
        model.addAttribute("totalContents", totalContents); // <-- 이 부분이 중요

        // 4. 페이지 계산
        int totalPages = (int) Math.ceil((double) totalContents / pageSize);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        // 5. 현재 페이지 글 추출
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalContents);
        List<LawContentDTO> contentList = allContents.subList(start, end);
        model.addAttribute("contentList", contentList);

        // 6. 페이지네이션 관련
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "category/categorylist";
    }

    @GetMapping("/content/{contentId}")
    public String contentDetail(@PathVariable("contentId") int contentId, Model model) {
        System.out.println("받은 contentId: " + contentId);

        LawContentDTO content = lawContentService.getContentById(contentId);
        System.out.println("조회된 콘텐츠: " + content.getTitle());

        lawContentService.increaseViewCount(contentId);

        model.addAttribute("content", content);

        return "category/contentDetail";
    }

    // 추가
    @GetMapping("/categoryViewCount")
    public String categoryViewCount(Model model) {
        List<CategoryDTO> list = categoryService.getCategoryViewCount();
        model.addAttribute("categoryList", list);
        return "category/categoryViewCount";
    }
}