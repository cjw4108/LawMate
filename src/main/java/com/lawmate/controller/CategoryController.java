package com.lawmate.controller;

import com.lawmate.dto.CategoryDto;
import com.lawmate.dto.LawContentDto;
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
        CategoryDto sch = new CategoryDto(0, "", "", null);
        model.addAttribute("categoryList", categoryService.categorySch(sch));
        return "category/category";
    }

    @GetMapping("/categorylist")
    public String categorylist(@RequestParam("categoryId") int categoryId, Model model) {
        System.out.println("받은 categoryId: " + categoryId);

        // categoryId로 직접 조회
        CategoryDto category = categoryService.getCategoryById(categoryId);
        System.out.println("조회된 카테고리: " + category.getName());

        model.addAttribute("category", category);

        // 해당 카테고리의 법률정보 목록 조회
        List<LawContentDto> contentList = lawContentService.getContentsByCategory(categoryId);
        System.out.println("법률정보 개수: " + contentList.size());

        model.addAttribute("contentList", contentList);

        return "category/categorylist";
    }

    @GetMapping("/categorySch")
    public String categorySch(@ModelAttribute("sch") CategoryDto sch, Model model) {
        model.addAttribute("categoryList", categoryService.categorySch(sch));
        return "category/category";
    }

    // 법률정보 상세 페이지
    @GetMapping("/content/{contentId}")
    public String contentDetail(@PathVariable("contentId") int contentId, Model model) {
        System.out.println("받은 contentId: " + contentId);

        // contentId로 법률정보 상세 조회
        LawContentDto content = lawContentService.getContentById(contentId);
        System.out.println("조회된 콘텐츠: " + content.getTitle());

        // 조회수 증가
        lawContentService.increaseViewCount(contentId);

        model.addAttribute("content", content);

        return "category/contentDetail";
    }



}