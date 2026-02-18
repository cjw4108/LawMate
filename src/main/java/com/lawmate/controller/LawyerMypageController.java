package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage/lawyer")
public class LawyerMypageController {

    // 변호사 마이페이지 메인
    @GetMapping("")
    public String lawyerMypage() {
        return "mypage/lawyer/mypage";
    }

    // 상담 진행 현황
    @GetMapping("/consult")
    public String consultManage() {
        return "mypage/lawyer/consult";
    }

    // 변호사 프로필 관리
    @GetMapping("/profile")
    public String profile() {
        return "mypage/lawyer/profile";
    }
}