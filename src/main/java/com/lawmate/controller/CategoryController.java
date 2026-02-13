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
}