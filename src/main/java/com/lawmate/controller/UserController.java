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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // [유지] 회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    // [유지] 변호사 가입 페이지 이동
    @GetMapping("/lawyer")
    public String lawyerSignupForm() {
        return "lawyer";
    }

    // [유지] 로그인 페이지 이동
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /* ================= 회원가입 처리 ================= */
    @PostMapping("/signup")
    public String signup(UserDTO user,
                         @RequestParam(value = "licenseFile", required = false) MultipartFile file,
                         Model model) {

        if ("LAWYER".equals(user.getRole()) && file != null && !file.isEmpty()) {
            try {
                String uploadDir = "C:/upload/license/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalName = file.getOriginalFilename();
                String saveName = UUID.randomUUID() + "_" + originalName;
                file.transferTo(new File(uploadDir + saveName));

                user.setLicenseFile(saveName);
            } catch (IOException e) {
                model.addAttribute("error", "파일 업로드 실패: " + e.getMessage());
                return "lawyer";
            }
        }

        boolean result = userService.signup(user);

        if (result) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "가입에 실패했습니다. 아이디 중복을 확인하세요.");
            return "signup";
        }
    }

    /* ================= 로그인 처리 ================= */
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

        // [유지] 변호사 승인 상태 체크 로직
        if ("LAWYER".equals(user.getRole()) && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/mypage";
        }

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}