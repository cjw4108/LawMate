package com.lawmate.controller;

import com.lawmate.dto.LawyerDTO;
import com.lawmate.dto.UserDTO;
import com.lawmate.service.LawyerService;
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
    private final LawyerService lawyerService; // ← 추가

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

    // --- 1. 일반 사용자 회원가입 ---
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

    @PostMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String userId) {
        boolean exists = userService.isUserIdExists(userId);
        return exists ? "duplicate" : "available";
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

    // --- 프로필 조회 ---
    @GetMapping("/api/user/profile")
    @ResponseBody
    public UserDTO getProfile(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return null;

        UserDTO user = userService.findByUserId(loginUser.getUserId());

        if ("ROLE_LAWYER".equals(user.getRole())) {
            LawyerDTO lawyer = lawyerService.getLawyerByEmail(user.getEmail()); // ← 이메일로 조회
            if (lawyer != null) {
                user.setSpecialty(lawyer.getSpecialty());
                user.setIntroduction(lawyer.getIntroduction());
            }
        }

        return user;
    }

    // --- 프로필 수정 ---
    @PutMapping("/api/user/profile")
    @ResponseBody
    public String updateProfile(@RequestBody UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "UNAUTHORIZED";

        userDTO.setId(loginUser.getId());
        userDTO.setRole(loginUser.getRole());

        System.out.println("받은 userDTO: " + userDTO);
        System.out.println("specialty: " + userDTO.getSpecialty());
        System.out.println("introduction: " + userDTO.getIntroduction());
        System.out.println("role: " + userDTO.getRole());
        System.out.println("id: " + userDTO.getId());

        userService.updateProfile(userDTO);

        loginUser.setName(userDTO.getName());
        loginUser.setEmail(userDTO.getEmail());
        loginUser.setPhone(userDTO.getPhone());
        session.setAttribute("loginUser", loginUser);

        return "OK";
    }
}