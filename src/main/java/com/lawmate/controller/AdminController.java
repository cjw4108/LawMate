package com.lawmate.controller;

import com.lawmate.dto.adminDTO;
import com.lawmate.dto.Question;
import com.lawmate.service.adminService;
import com.lawmate.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 서비스 주입을 위해 추가
public class AdminController {

    private final QuestionService questionService; // 서비스 연결
    private final adminService adminService;

    /* ================= 관리자 로그인 ================= */

    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/adminLogin";
    }

    @PostMapping("/login")
    public String adminLogin(
            @RequestParam String adminId,
            @RequestParam String adminPw,
            HttpSession session,
            Model model) {

        adminDTO admin = adminService.login(adminId, adminPw);

        if (admin == null) {
            model.addAttribute("error", "관리자 아이디 또는 비밀번호가 올바르지 않습니다.");
            return "admin/adminLogin";
        }

        session.setAttribute("loginAdmin", admin);
        return "redirect:/admin/main";
    }
    @GetMapping("/main")
    public String adminMain() {
        return "admin/adminMain";
    }

    // 게시판 운영 관리 (신고 리스트 출력)
    @GetMapping("/qna")
    public String adminQna(Model model) {
        // 실제 신고된 데이터를 가져와서 모델에 담습니다.
        List<Question> reportedList = questionService.findReportedQuestions();
        model.addAttribute("reportedList", reportedList);

        // 기존 return 경로가 admin/adminQna라면 그대로 유지
        return "admin/adminQna";
    }

    @GetMapping("/users")
    public String adminUsers() {
        return "admin/adminUsers";
    }
}
