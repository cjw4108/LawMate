package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryListController {

    @GetMapping("/categorylist")
    public String categorylist() {
        return "categorylist"; // → /WEB-INF/views/categorylist.jsp
    }
}
