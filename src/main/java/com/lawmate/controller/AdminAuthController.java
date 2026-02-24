package com.lawmate.controller;

import com.lawmate.dto.AdminDTO;
import com.lawmate.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService; // 대문자 시작에서 소문자로 관례적 변경

    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/adminLogin";
    }

    @PostMapping("/login")
    public String adminLogin(AdminDTO adminDTO, HttpSession session) {
        AdminDTO loginAdmin = AdminService.login(adminDTO);

        if (loginAdmin == null) {
            session.setAttribute("errorMsg", "관리자 정보가 올바르지 않습니다.");
            return "redirect:/admin/login";
        }

        // 1. 세션 저장 (키값: loginAdmin)
        session.setAttribute("loginAdmin", loginAdmin);

        // 2. 승인 관리 페이지로 이동 (본인의 @GetMapping("/approve") 경로와 일치)
        return "redirect:/admin/lawyer/approve";
    }
}