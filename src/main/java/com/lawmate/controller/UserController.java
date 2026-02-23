package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// UserController.java (수정 후)
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ================= 회원가입 처리 ================= */
    @PostMapping("/signup")
    public String signup(UserDTO user,
                         @RequestParam(value = "licenseFile", required = false) MultipartFile file,
                         Model model) {
        // ... (기존 파일 업로드 및 서비스 호출 로직 유지) ...
        boolean result = userService.signup(user);
        if (result) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "가입에 실패했습니다.");
            return "signup";
        }
    }

    /* ================= 로그인 처리 ================= */
    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password,
                        HttpSession session, Model model) {
        UserDTO user = userService.login(userId, password);
        // ... (기존 로그인 처리 로직 유지) ...
        return "redirect:/main";
    }

    // 로그아웃 처리는 기능이므로 여기에 유지
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // [중요] 아래 메서드들은 삭제합니다 (UserPageController와 중복됨)
    // public String signupForm() { ... }
    // public String lawyerSignupForm() { ... }
    // public String loginForm() { ... }
}