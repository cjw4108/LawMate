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
    @GetMapping("/signup/user")
    public String signupUser() {
        return "signup_user";
        // → /WEB-INF/views/signup_user.jsp
    }

    // 변호사 회원가입
    @GetMapping("/signup/lawyer")
    public String signupLawyer() {
        return "signup_lawyer";
        // → /WEB-INF/views/signup_lawyer.jsp
    }
}