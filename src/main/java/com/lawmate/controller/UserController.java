package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

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

        if ("정지".equals(user.getStatus())) {
            model.addAttribute("error", "정지된 계정입니다. 관리자에게 문의하세요.");
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
        user.setStatus("정상");

        if (userService.signup(user)) {
            return "redirect:/login?msg=success";
        }

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

        try {
            // 1. 파일을 getBytes()로 메모리에 먼저 읽은 후 저장
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String uploadDir = "C:/lawmate/uploads/license/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String savedFileName = UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();
                byte[] bytes = uploadFile.getBytes();
                Files.write(Paths.get(uploadDir + savedFileName), bytes);

                user.setLicenseFile(savedFileName);
            }

            // 2. 변호사 기본 정보 세팅
            user.setJoinDate(LocalDate.now());
            user.setRole("ROLE_LAWYER");
            user.setLawyerStatus("PENDING");
            user.setStatus("정상");

            // 3. DB 저장 (파일은 이미 저장했으므로 null 전달)
            if (userService.signupLawyer(user, null)) {
                return "redirect:/login?msg=pending";
            }

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "파일 저장 중 서버 오류가 발생했습니다.");
            return "lawyer";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "가입 처리 중 알 수 없는 오류가 발생했습니다.");
            return "lawyer";
        }

        model.addAttribute("error", "회원가입 신청 실패");
        return "lawyer";
    }

    // 7. 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}