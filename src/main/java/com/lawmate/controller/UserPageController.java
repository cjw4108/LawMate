package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// UserPageController.java (현상태 유지 또는 메서드명 정리)
//@Controller
public class UserPageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupUser() {
        return "signup";
    }

    @GetMapping("/lawyer")
    public String lawyerSignup() {
        return "lawyer";
    }
}