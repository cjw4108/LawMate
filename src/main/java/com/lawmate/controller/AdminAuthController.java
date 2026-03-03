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
        AdminDTO loginAdmin = adminService.login(adminDTO); //

        if (loginAdmin == null) {
            session.setAttribute("errorMsg", "관리자 정보가 올바르지 않습니다.");
            return "redirect:/admin/login";
        }

        session.setAttribute("loginAdmin", loginAdmin);

        // ✅ /admin/approve 가 아니라 /admin/lawyer/approve 로 리다이렉트 해야 합니다.
        return "redirect:/admin/lawyer/approve"; //
    }
}