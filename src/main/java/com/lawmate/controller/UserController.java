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

        // 세션 키 "loginUser"로 통일 (UserPageController, LawyerMypageController 와 일치)
        session.setAttribute("loginUser", user);

        String role = user.getRole();

        // 권한별 리다이렉트 처리
        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/main";
        } else if ("ROLE_LAWYER".equals(role)) {
            // 변호사인데 아직 승인 대기중인 경우
            if ("PENDING".equals(user.getLawyerStatus())) {
                model.addAttribute("error", "현재 자격 승인 심사 중입니다.");
                session.invalidate();
                return "login";
            }
            return "redirect:/home"; // 변호사 마이페이지로 이동
        } else {
            return "redirect:/home"; // 일반회원 마이페이지로 이동
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
        return "lawyer"; // lawyer.jsp 호출
    }

    // 5. 일반 사용자 회원가입 처리
    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam String passwordConfirm, Model model) {
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        user.setJoinDate(LocalDate.now());
        user.setRole("ROLE_USER");
        user.setLawyerStatus("NONE");

        if (userService.signup(user)) return "redirect:/login?msg=success";

        model.addAttribute("error", "아이디 중복 또는 가입 실패");
        return "signup";
    }

    // 6. 변호사 회원가입 처리
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
