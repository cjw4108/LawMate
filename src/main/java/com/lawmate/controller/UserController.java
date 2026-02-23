package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam(value="licenseFile", required=false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            user.setRole("ROLE_LAWYER");
            user.setLawyerStatus("PENDING");
            // 파일 저장 로직은 기존대로 유지
        } else {
            user.setRole("ROLE_USER");
            user.setLawyerStatus("APPROVED");
        }
        userService.signup(user);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, Model model) {
        UserDTO user = userService.login(userId, password);
        if (user == null) {
            model.addAttribute("error", "로그인 실패");
            return "login";
        }
        if ("ROLE_LAWYER".equals(user.getRole()) && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/login?error=pending";
        }
        session.setAttribute("loginUser", user);
        return "redirect:/main";
    }
}