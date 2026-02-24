package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.ConsultService;
import com.lawmate.service.DocumentLoaderService;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;
    private final ConsultService consultService;
    private final DocumentLoaderService documentService;

    // ✔ 마이페이지 메인
    // 접속 URL: localhost:8080/mypage/my-page
    @GetMapping("/my-page")
    public String userDashboard(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        // 로그인 체크
        if (loginUser == null) {
            return "redirect:/login";
        }

        String userId = loginUser.getUserId();

        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("consultCount", consultService.getConsultCountByUserId(userId));
        model.addAttribute("consultList", consultService.getConsultListByUserId(userId));
        model.addAttribute("docList", documentService.getUserDownloadList(userId));

        // JSP 경로
        return "mypage/user/mypage";
    }

    // ✔ 프로필 수정
    @PostMapping("/profile/update")
    public String updateProfile(UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        userDTO.setUserId(loginUser.getUserId());
        userService.updateProfile(userDTO);

        // 세션 갱신
        session.setAttribute(
                "loginUser",
                userService.getUserById(loginUser.getUserId())
        );

        return "redirect:/mypage/my-page";
    }
}
