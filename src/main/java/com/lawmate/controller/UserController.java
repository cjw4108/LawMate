package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    @PostMapping("/signup")
    public String signup(
            @RequestParam String userId,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String email,
            Model model
    ) {
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "signup";
        }

        UserDTO user = new UserDTO(userId, password, email, "USER");

        if (!userService.register(user)) {
            model.addAttribute("error", "이미 존재하는 아이디입니다");
            return "signup";
        }

        return "redirect:/login";
    }

    /* ================= 변호사 회원가입 ================= */
    @PostMapping("/lawyer/signup")
    public String lawyerSignup(
            @RequestParam String userId,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String email,
            @RequestParam("proofFile") MultipartFile proofFile,
            Model model
    ) throws IOException {

        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "lawyerSignup";
        }

        if (proofFile.isEmpty()) {
            model.addAttribute("error", "자격 증빙 파일은 필수입니다");
            return "lawyerSignup";
        }

        String uploadDir = "C:/upload/lawyer/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + proofFile.getOriginalFilename();
        File saveFile = new File(uploadDir + fileName);
        proofFile.transferTo(saveFile);

        UserDTO lawyer = new UserDTO(userId, password, email, "LAWYER");
        lawyer.setLawyerStatus("PENDING");
        lawyer.setProofFilePath(saveFile.getAbsolutePath());

        if (!userService.register(lawyer)) {
            model.addAttribute("error", "이미 존재하는 아이디입니다");
            return "lawyerSignup";
        }

        return "redirect:/login";
    }
}