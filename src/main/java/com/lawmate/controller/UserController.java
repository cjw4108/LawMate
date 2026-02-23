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

    // 회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    // 변호사 가입 페이지 이동
    @GetMapping("/lawyer")
    public String lawyerSignupForm() {
        return "lawyer";
    }

    /* ================= 회원가입 처리 ================= */
    @PostMapping("/signup")
    public String signup(UserDTO user,
                         @RequestParam(value = "licenseFile", required = false) MultipartFile file,
                         Model model) {

        // 1. 변호사 가입 시 파일 업로드 처리
        if ("LAWYER".equals(user.getRole()) && file != null && !file.isEmpty()) {
            try {
                // 프로젝트 외부에 파일을 저장하는 것이 좋습니다. (예: C:/lawmate/upload/)
                String uploadDir = "C:/upload/license/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalName = file.getOriginalFilename();
                String saveName = UUID.randomUUID() + "_" + originalName;
                file.transferTo(new File(uploadDir + saveName));

                // DB에 저장할 파일명 설정
                user.setLicenseFile(saveName);
            } catch (IOException e) {
                model.addAttribute("error", "파일 업로드 실패: " + e.getMessage());
                return "lawyer";
            }
        }

        // 2. 서비스 호출
        boolean result = userService.signup(user);

        if (result) {
            return "redirect:/login"; // 성공 시 로그인 페이지로
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

        // 세션 정보 저장
        session.setAttribute("loginUser", user);
        session.setAttribute("role", user.getRole());

        // 변호사인데 승인이 안 된 경우 마이페이지로 보내서 확인하게 함
        if ("LAWYER".equals(user.getRole()) && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/mypage"; // 혹은 승인 대기 안내 페이지
        }

        return "redirect:/main";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}