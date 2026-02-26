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

    private final AdminService adminService;

    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/adminLogin";
    }

    @PostMapping("/login")
    public String adminLogin(AdminDTO adminDTO, HttpSession session) {
        // ✅ 수정: AdminService.login(adminDTO) -> adminService.login(adminDTO)
        // 클래스 레벨의 static 호출이 아닌 주입받은 빈(Bean)을 통한 호출로 변경했습니다.
        AdminDTO loginAdmin = adminService.login(adminDTO);

        if (loginAdmin == null) {
            session.setAttribute("errorMsg", "관리자 정보가 올바르지 않습니다.");
            return "redirect:/admin/login";
        }

        // 1. 세션 저장 (키값: loginAdmin)
        session.setAttribute("loginAdmin", loginAdmin);

        // 2. 승인 관리 페이지로 이동
        return "redirect:/admin/lawyer/approve";
    }
}