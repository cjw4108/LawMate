package com.lawmate.controller;

import com.lawmate.entity.UserEntity;
import com.lawmate.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ⭐ 반드시 이 경로여야 합니다 (org.springframework.ui.Model)
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @GetMapping
    public String userList(@RequestParam(defaultValue = "all") String filter,
                           @RequestParam(required = false) String keyword,
                           Model model) { // 여기서 사용되는 Model 타입이 위 임포트와 일치해야 함

        List<UserEntity> userList = userService.getUsers(filter, keyword);

        // 이제 'addAttribute'를 정상적으로 인식합니다.
        model.addAttribute("userList", userList);
        model.addAttribute("currentFilter", filter);

        return "admin/adminUsers";
    }

    @PostMapping("/status")
    @ResponseBody
    public String updateUserStatus(@RequestParam String userId, @RequestParam String status) {
        try {
            userService.changeStatus(userId, status); // 위에서 만든 service 호출
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }
}