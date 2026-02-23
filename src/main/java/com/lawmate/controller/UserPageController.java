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
@RequestMapping("/mypage/user")
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;
    private final ConsultService consultService;
    private final DocumentLoaderService documentService;

    // 마이페이지 메인 - 모든 탭 데이터 한번에 전달
    @GetMapping("")
    public String userDashboard(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"ROLE_USER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        String userId = loginUser.getUserId();

        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("consultCount", consultService.getConsultCountByUserId(userId));
        model.addAttribute("consultList", consultService.getConsultListByUserId(userId));  // 상담 탭용
        model.addAttribute("docList", documentService.getUserDownloadList(userId));         // 문서 탭용

        return "mypage/user/mypage";
    }

    // 프로필 수정 처리
    @PostMapping("/profile/update")
    public String updateProfile(UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        userDTO.setUserId(loginUser.getUserId());
        userService.updateProfile(userDTO);

        // 세션 정보 최신화
        session.setAttribute("loginUser", userService.getUserById(loginUser.getUserId()));
        return "redirect:/user/mypage";
    }

    // 아래는 혹시 팀원들이 개별 URL로 쓸 경우를 위해 유지
    @GetMapping("/consult")
    public String userConsultList(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_USER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("consultList", consultService.getConsultListByUserId(loginUser.getUserId()));
        return "mypage/user/consult";
    }

    @GetMapping("/docs")
    public String userDocuments(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_USER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("docList", documentService.getUserDownloadList(loginUser.getUserId()));
        return "mypage/user/docs";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_USER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserById(loginUser.getUserId()));
        return "mypage/user/profile";
    }
}
