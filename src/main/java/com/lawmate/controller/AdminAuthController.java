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
        // static 방식이 아닌 주입받은 service 인스턴스 사용
        AdminDTO loginAdmin = adminService.login(adminDTO);

        if (loginAdmin == null) {
            session.setAttribute("errorMsg", "관리자 정보가 올바르지 않습니다.");
            return "redirect:/admin/login";
        }

        session.setAttribute("loginAdmin", loginAdmin);
        // 이동할 메인 페이지 경로 확인 필요
        return "redirect:/admin/main";
    }
}