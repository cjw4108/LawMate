package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user/profile")
    @ResponseBody
    public ResponseEntity<?> getProfile(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        UserDTO profile = userService.findByUserId(loginUser.getUserId());
        if (profile == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/api/user/profile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        userDTO.setUserId(loginUser.getUserId());
        userService.updateProfile(userDTO);
        // 세션 정보도 최신화
        loginUser.setName(userDTO.getName());
        loginUser.setEmail(userDTO.getEmail());
        loginUser.setPhone(userDTO.getPhone());
        session.setAttribute("loginUser", loginUser);
        return ResponseEntity.ok("success");
    }

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
