package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        UserDTO user = userService.login(userId, password);

        // 1. 아이디/비밀번호가 틀린 경우
        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀립니다.");
            return "login";
        }

        // 2. 계정 상태가 '정지'인 경우
        if ("정지".equals(user.getStatus())) {
            model.addAttribute("error", "관리자에 의해 이용이 제한된 계정입니다.");
            return "login";
        }

        // 3. 변호사 승인 대기중인 경우
        if ("ROLE_LAWYER".equals(user.getRole()) && "승인대기".equals(user.getStatus())) {
            model.addAttribute("error", "현재 자격 승인 심사 중입니다. 관리자 승인 후 이용 가능합니다.");
            return "login";
        }

        // --- [수정 구간] 로그인 성공 시 세션 저장 ---
        session.setAttribute("loginUser", user);
        // ⭐ 중요: AdminInterceptor가 권한을 확인할 수 있도록 userRole을 세션에 개별 저장합니다.
        session.setAttribute("userRole", user.getRole());

        // 권한별 리다이렉트 (기현 님 요청 사항: 관리자는 바로 관리자 페이지로)
        if ("ROLE_ADMIN".equals(user.getRole())) {
            return "redirect:/admin/main";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/signup/lawyer")
    public String signupLawyerPage() {
        return "lawyer";
    }

    // --- 1. 일반 사용자 회원가입 (기존 유지: 가입 즉시 정상) ---
    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam String passwordConfirm, HttpSession session, Model model) {
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        user.setJoinDate(LocalDate.now());
        user.setStatus("정상");
        user.setRole("ROLE_USER");

        if (userService.signup(user)) {
            session.setAttribute("loginUser", user);
            // 일반 사용자 가입 시에도 인터셉터 통과를 위해 Role을 담아주는 것이 좋습니다.
            session.setAttribute("userRole", user.getRole());
            return "redirect:/home?msg=welcome";
        }
        model.addAttribute("error", "아이디 중복 또는 가입 실패");
        return "signup";
    }

    @PostMapping("/signup/lawyer")
    public String signupLawyer(UserDTO user,
                               @RequestParam String passwordConfirm,
                               @RequestParam("uploadFile") MultipartFile uploadFile,
                               Model model) {

        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "lawyer";
        }

        user.setJoinDate(LocalDate.now());
        user.setStatus("승인대기");
        user.setRole("ROLE_LAWYER");
        user.setLawyerStatus("PENDING");

        if (userService.signupLawyer(user, uploadFile)) {
            return "redirect:/login?msg=pending";
        }

        model.addAttribute("error", "회원가입 신청 중 오류가 발생했습니다.");
        return "lawyer";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}