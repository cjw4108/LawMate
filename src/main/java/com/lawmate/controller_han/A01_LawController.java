package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// http://localhost:8080/search
@Controller
public class A01_LawController {
    @GetMapping("/search")
    public String index() {
        return "Han/index_han";
    }
}