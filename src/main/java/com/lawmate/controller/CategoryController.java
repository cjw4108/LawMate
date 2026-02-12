package com.lawmate.controller;

import com.lawmate.dto.CategoryDto;
import com.lawmate.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/category")
    public String category(Model model) {
        // 전체 카테고리 조회 (빈 검색 조건)
        CategoryDto sch = new CategoryDto(0, "", "", null);
        model.addAttribute("categoryList", service.categorySch(sch));
        return "category/category";
    }

    @GetMapping("/categorySch")
    public String categorySch(@ModelAttribute("sch") CategoryDto sch, Model model) {
        model.addAttribute("categoryList", service.categorySch(sch));
        return "category/category";
    }

    @GetMapping("/categorylist")
    public String categorylist() {
        return "category/categorylist";
    }
}