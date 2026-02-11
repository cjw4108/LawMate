package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category")
    public String category() {
        return "category/category"; // → /WEB-INF/views/category/category.jsp
    }

    @GetMapping("/categorylist")
    public String categorylist() {
        return "category/categorylist"; // → /WEB-INF/views/category/categorylist.jsp
    }
}
