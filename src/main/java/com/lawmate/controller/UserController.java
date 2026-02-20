package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /* ================= 로그인 ================= */
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
        session.setAttribute("lawyerStatus", user.getLawyerStatus());

        if ("LAWYER".equals(user.getRole())
                && !"APPROVED".equals(user.getLawyerStatus())) {
            return "redirect:/lawyer/pending";
        }

        return "redirect:/main";
    }

    /* ================= 일반 회원가입 ================= */
    @Controller
    @RequestMapping("/user")
    @RequiredArgsConstructor
    public class UserController {

        private final UserService userService;

        @GetMapping("/signup")
        public String signupForm() {
            return "user/userSignup";
        }

        @PostMapping("/signup")
        public String signup(UserDTO user) {
            user.setRole("USER");
            userService.signup(user);
            return "redirect:/login";
        }
    }

    /* ================= 변호사 회원가입 ================= */
    @Controller
    @RequestMapping("/lawyer")
    @RequiredArgsConstructor
    public class LawyerController {

        private final UserService userService;

        @GetMapping("/signup")
        public String signupForm() {
            return "lawyer/lawyerSignup";
        }

        @PostMapping("/signup")
        public String signup(UserDTO user) {
            user.setRole("LAWYER");
            user.setLawyerStatus("PENDING");
            userService.signup(user);
            return "redirect:/login";
        }
    }