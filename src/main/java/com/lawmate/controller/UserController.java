package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login") public String loginForm() { return "login"; }
    @GetMapping("/signup") public String signupForm() { return "signup"; }
    @GetMapping("/lawyer") public String lawyerForm() { return "lawyer"; }

    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam(value="licenseFile", required=false) MultipartFile file) {
        if ("LAWYER".equals(user.getRole()) && file != null && !file.isEmpty()) {
            try {
                String path = "C:/upload/license/"; // 🔴 반드시 이 폴더를 직접 만드세요!
                new File(path).mkdirs();
                String saveName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                file.transferTo(new File(path + saveName));
                user.setLicenseFile(saveName);
            } catch (Exception e) { e.printStackTrace(); }
        }
        if (userService.signup(user)) return "redirect:/login";
        return "signup";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, Model model) {
        UserDTO user = userService.login(userId, password);
        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀립니다.");
            return "login";
        }
        session.setAttribute("loginUser", user);
        session.setAttribute("role", user.getRole());

        // 변호사 미승인 시 리다이렉트 로직
        if ("LAWYER".equals(user.getRole()) && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/login?error=pending";
        }
        return "redirect:/main";
    }
}