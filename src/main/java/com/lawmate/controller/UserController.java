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


      @GetMapping("/login")
     public String loginForm() { return "login"; }

    @GetMapping("/signup")
    public String signupForm() { return "signup"; }

    @GetMapping("/lawyer")
    public String lawyerForm() { return "lawyer"; }

    /* 회원가입 처리 */
    @PostMapping("/signup")
    public String signup(UserDTO user, @RequestParam(value = "licenseFile", required = false) MultipartFile file, Model model) {
        if ("LAWYER".equals(user.getRole()) && file != null && !file.isEmpty()) {
            try {
                String uploadDir = "C:/upload/license/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                String saveName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadDir + saveName));
                user.setLicenseFile(saveName);
            } catch (IOException e) {
                model.addAttribute("error", "파일 업로드 실패");
                return "lawyer";
            }
        }
        boolean result = userService.signup(user);
        if (result) return "redirect:/login"; // 가입 성공 시 로그인 페이지로
        model.addAttribute("error", "가입 실패: 중복된 아이디입니다.");
        return "LAWYER".equals(user.getRole()) ? "lawyer" : "signup";
    }

    /* 로그인 처리 */
    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, Model model) {
        UserDTO user = userService.login(userId, password);
        if (user != null) {
            // 팀원들과 공유할 세션 키값: "loginUser"
            session.setAttribute("loginUser", user);
            session.setAttribute("role", user.getRole());
            return "redirect:/home";
        }
        model.addAttribute("error", "로그인 정보가 틀렸습니다.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}