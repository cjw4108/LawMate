package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
        // → /WEB-INF/views/login.jsp
    }

    // 일반 회원가입
    @GetMapping("/signup")
    public String signupUser() {
        return "signup";
        // → /WEB-INF/views/signup.jsp
    }

    // 변호사 회원가입
    @GetMapping("/lawyer")
    public String Lawyer() {
        return "lawyer";
        // → /WEB-INF/views/lawyer.jsp
    }
}