package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ================= 로그인 ================= */
    @PostMapping("/login")
    public String login(
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        UserDTO user = userService.login(userId, password);

        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호 오류");
            return "login";
        }

        session.setAttribute("loginUser", user);
        session.setAttribute("role", user.getRole());
        session.setAttribute("lawyerStatus", user.getLawyerStatus());

        if ("LAWYER".equals(user.getRole())
                && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/lawyer/pending";
        }

        return "redirect:/main";
    }
}