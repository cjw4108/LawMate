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

    private final AdminService AdminService;

    // 관리자 로그인 페이지
    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/adminLogin";
    }

    // 관리자 로그인 처리
    @PostMapping("/login")
    public String adminLogin(AdminDTO adminDTO,
                             HttpSession session) {

        AdminDTO loginAdmin = com.lawmate.service.AdminService. login(adminDTO);

        if (loginAdmin == null) {
            session.setAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/admin/login";
        }

        // 관리자 세션 저장 (완전 분리)
        session.setAttribute("loginAdmin", loginAdmin);

        return "redirect:/admin/approve";
    }

    // 관리자 로그아웃
    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}