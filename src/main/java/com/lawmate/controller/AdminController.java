package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // 관리자 메인 (현황 요약)
    @GetMapping("/main")
    public String adminMain() {
        return "admin/adminMain";
    }

    // 게시판 운영 관리
    @GetMapping("/qna")
    public String adminQna() {
        return "admin/adminQna";
    }

    // 사용자 관리
    @GetMapping("/users")
    public String adminUsers() {
        return "admin/adminUsers";
    }
}