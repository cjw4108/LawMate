package com.lawmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.lawmate.service.LawContentService;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @Autowired
    private LawContentService service;
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("popularList", service.getTopByViewCount(2));
        return "home"; // → /WEB-INF/views/home.jsp
    }
}
