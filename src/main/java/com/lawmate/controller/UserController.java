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

        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀립니다.");
            return "login";
        }

        if ("정지".equals(user.getStatus())) {
            model.addAttribute("error", "관리자에 의해 이용이 제한된 계정입니다.");
            return "login";
        }

        if ("ROLE_LAWYER".equals(user.getRole()) && "승인대기".equals(user.getStatus())) {
            model.addAttribute("error", "현재 자격 승인 심사 중입니다. 관리자 승인 후 이용 가능합니다.");
            return "login";
        }

        session.setAttribute("loginUser", user);
        session.setAttribute("userRole", user.getRole());

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

    // --- [추가] 기현님 요청 사항: 아이디 중복 체크 전용 메서드 ---
    @PostMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam("userId") String userId) {
        boolean isDuplicate = userService.idCheck(userId);
        return isDuplicate ? "duplicated" : "available";
    }

    // --- [수정] 일반 사용자 회원가입 로직 ---
    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam String passwordConfirm, HttpSession session, Model model) {
        // 1. 비밀번호 일치 확인
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        // 2. 아이디 중복 체크 (기현님이 지적한 20라인 문제 해결)
        if (userService.idCheck(user.getUserId())) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다.");
            return "signup";
        }

        user.setJoinDate(LocalDate.now());
        user.setStatus("정상");
        user.setRole("ROLE_USER");

        // 3. 가입 처리
        if (userService.signup(user)) {
            session.setAttribute("loginUser", user);
            session.setAttribute("userRole", user.getRole());
            return "redirect:/home?msg=welcome";
        }

        // 4. 기타 서버 오류
        model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다.");
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

        // 변호사 가입 시에도 중복 체크 추가
        if (userService.idCheck(user.getUserId())) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다.");
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