package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // 일반 회원가입 JSP
    }
}