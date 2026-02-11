package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category")
    public String category() {
        return "category/category"; // → /WEB-INF/views/ccategory.jsp
    }
}
