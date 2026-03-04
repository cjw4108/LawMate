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

        // 2. 계정 상태가 '정지'인 경우 (차단 로직)
        if ("정지".equals(user.getStatus())) {
            model.addAttribute("error", "관리자에 의해 이용이 제한된 계정입니다.");
            return "login";
        }

        // 3. ⭐ 변호사인데 아직 승인 대기중인 경우 (핵심 차단 로직)
        // ROLE_LAWYER이면서 상태가 '승인대기'이면 로그인을 허용하지 않습니다.
        if ("ROLE_LAWYER".equals(user.getRole()) && "승인대기".equals(user.getStatus())) {
            model.addAttribute("error", "현재 자격 승인 심사 중입니다. 관리자 승인 후 이용 가능합니다.");
            return "login";
        }

        // 로그인 성공 시 세션 저장
        session.setAttribute("loginUser", user);

        // 권한별 리다이렉트
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
        user.setStatus("정상"); // 일반 사용자는 바로 '정상'
        user.setRole("ROLE_USER");

        if (userService.signup(user)) {
            session.setAttribute("loginUser", user);
            return "redirect:/home?msg=welcome";
        }
        model.addAttribute("error", "아이디 중복 또는 가입 실패");
        return "signup";
    }

    // --- 2. 변호사 회원가입 (수정: 가입 시 '승인대기' 상태로 변경) ---
    @PostMapping("/signup/lawyer")
    public String signupLawyer(UserDTO user,
                               @RequestParam String passwordConfirm,
                               @RequestParam("uploadFile") MultipartFile uploadFile,
                               Model model) { // 자동 로그인을 방지하기 위해 session 제거

        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "lawyer";
        }

        // ⭐ 핵심 변경: 상태값을 "승인대기" 및 "PENDING"으로 설정
        user.setJoinDate(LocalDate.now());
        user.setStatus("승인대기");       // 로그인 차단 기준이 됨
        user.setRole("ROLE_LAWYER");
        user.setLawyerStatus("PENDING"); // 관리자 페이지 목록에 노출될 기준

        if (userService.signupLawyer(user, uploadFile)) {
            // ✅ 변호사는 승인이 필요하므로 자동 로그인을 시키지 않고 로그인 페이지로 보냄
            // msg=pending 파라미터를 넘겨서 알림을 띄울 수 있게 함
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