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

    // 1. 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 2. 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        UserDTO user = userService.login(userId, password);

        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀립니다.");
            return "login";
        }

        session.setAttribute("loginUser", user);
        String role = user.getRole();

        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/main";
        } else if ("ROLE_LAWYER".equals(role)) {
            if ("PENDING".equals(user.getLawyerStatus())) {
                model.addAttribute("error", "현재 자격 승인 심사 중입니다.");
                session.invalidate();
                return "login";
            }
            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }

    // 3. 일반 사용자 회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // 4. 변호사 회원가입 페이지 이동
    @GetMapping("/signup/lawyer")
    public String signupLawyerPage() {
        return "lawyer";
    }

    // 5. 일반 사용자 회원가입 처리 (수정됨)
    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam String passwordConfirm, Model model) {
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        user.setJoinDate(LocalDate.now());
        user.setRole("ROLE_USER");
        user.setLawyerStatus("NONE");

        // ★ 추가: 가입 시 상태를 "정상"으로 설정
        user.setStatus("정상");

        // UserDTO에 userName, userPhone 필드가 있다면
        // Spring이 JSP의 input name과 매칭하여 자동으로 담아줍니다.
        if (userService.signup(user)) return "redirect:/login?msg=success";

        model.addAttribute("error", "아이디 중복 또는 가입 실패");
        return "signup";
    }

    // 6. 변호사 회원가입 처리 (수정됨)
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
        user.setRole("ROLE_LAWYER");
        user.setLawyerStatus("PENDING");

        // ★ 추가: 변호사 가입 시에도 상태를 "정상"으로 설정
        user.setStatus("정상");

        if (userService.signupLawyer(user, uploadFile)) {
            return "redirect:/login?msg=pending";
        }

        model.addAttribute("error", "회원가입 신청 중 오류가 발생했습니다.");
        return "lawyer";
    }

    // 7. 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}