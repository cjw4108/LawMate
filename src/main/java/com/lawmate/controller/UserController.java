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

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 화면 이동용 (GET)
    @GetMapping("/login")
    public String loginForm() { return "login"; }

    @GetMapping("/signup")
    public String signupForm() { return "signup"; }

    @GetMapping("/lawyer")
    public String lawyerForm() { return "lawyer"; }

    // 2. 데이터 처리용 (POST)
    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, Model model) {
        UserDTO user = userService.login(userId, password);
        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/main";
        }
        model.addAttribute("error", "로그인 실패");
        return "login";
    }

    @PostMapping("/signup")
    public String signup(UserDTO user) {
        userService.signup(user);
        return "redirect:/login";
    }
}