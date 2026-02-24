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

    // 1. 마이페이지 메인 접속 URL: localhost:8080/mypage/docs
    @GetMapping("/docs")
    public String userDashboard(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        // 세션 체크 (로그인 안 되어 있으면 로그인 페이지로)
        if (loginUser == null) {
            return "redirect:/login";
        }

        String userId = loginUser.getUserId();

        // DB 데이터 연동
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("consultCount", consultService.getConsultCountByUserId(userId));
        model.addAttribute("consultList", consultService.getConsultListByUserId(userId));
        model.addAttribute("docList", documentService.getUserDownloadList(userId));

        // 뷰 파일 위치: src/main/resources/templates/mypage/user/mypage.html 확인!
        return "mypage/user/mypage";
    }

    // 2. 프로필 수정 처리
    @PostMapping("/profile/update")
    public String updateProfile(UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        userDTO.setUserId(loginUser.getUserId());
        userService.updateProfile(userDTO);

        // 세션 정보 최신화 후 다시 메인으로 리다이렉트
        session.setAttribute("loginUser", userService.getUserById(loginUser.getUserId()));
        String s = "redirect:/mypage/docs";
        return s;
    }
}
