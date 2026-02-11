package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LawyerController {

    @GetMapping("/lawyer")
    public String lawyer() {
        return "lawyer"; // 변호사 회원가입 JSP
    }
}
