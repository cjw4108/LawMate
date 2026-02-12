package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage/user")
public class UserMypageController {

    // 일반 회원 마이페이지 메인
    @GetMapping("")
    public String userMypage() {
        return "mypage/user/mypage";
    }

    // 상담 내역
    @GetMapping("/consult")
    public String consultList() {
        return "mypage/user/consult";
    }

    // 회원 정보
    @GetMapping("/profile")
    public String profile() {
        return "mypage/user/profile";
    }
}
