package com.lawmate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,

            Model model) {

        UserDTO loginUser = userService.login(userId, password);

        if(loginUser == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login"; // 로그인 페이지로 다시
        }



        // 권한별 화면 분기
        String role = loginUser.getRole();
        switch(role) {
            case "ADMIN": return "redirect:/admin/main";
            case "LAWYER": return "redirect:/lawyer/main";
            default: return "redirect:/user/main";
        }
    }
}
