package com.lawmate.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class SearchController {

    @GetMapping("/search")
    public String search() {
        return "search"; // → /WEB-INF/views/search.jsp
    }
}
