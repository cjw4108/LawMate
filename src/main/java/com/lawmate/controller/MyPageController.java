package com.lawmate.controller;

import jakarta.servlet.http.HttpSession;

import com.lawmate.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping("/mypage")
    public String showMyPage(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        String role = loginUser.getRole();
        if ("ROLE_LAWYER".equals(role)) {
            return "mypage_lawyer";
        } else {
            return "mypage_user";
        }
    }
}