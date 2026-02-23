package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.ConsultService;
import com.lawmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/mypage/lawyer")
@RequiredArgsConstructor
public class LawyerMypageController {

    private final UserService userService;
    private final ConsultService consultService;

    // 1. 변호사 마이페이지 메인 (대시보드)
    @GetMapping("")
    public String lawyerMypage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_LAWYER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        // DB에서 최신 정보를 가져와 화면에 전달 (승인 상태 포함)
        UserDTO user = userService.getUserById(loginUser.getUserId());
        model.addAttribute("user", user);

        return "mypage/lawyer/mypage";
    }

    // 2. 상담 진행 현황 목록
    @GetMapping("/consult")
    public String consultManage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_LAWYER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        // 해당 변호사 ID로 배정된 상담 리스트 조회
        model.addAttribute("consultList", consultService.getLawyerConsultList(loginUser.getUserId()));
        return "mypage/lawyer/consult";
    }

    // 3. 변호사 프로필 조회
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_LAWYER".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("user", userService.getUserById(loginUser.getUserId()));
        return "mypage/lawyer/profile";
    }

    // 4. 프로필 정보 업데이트 (이름, 연락처 등)
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        userDTO.setUserId(loginUser.getUserId());
        userService.updateProfile(userDTO);

        // 세션 정보 최신화
        session.setAttribute("loginUser", userService.getUserById(loginUser.getUserId()));
        return "redirect:/mypage/lawyer/profile";
    }

    // 5. 자격 신청/재신청 처리 (파일 업로드)
    @PostMapping("/cert/apply")
    public String applyCert(@RequestParam("licenseFile") MultipartFile licenseFile,
                            HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        try {
            // 기존 signupLawyer 로직을 재활용하여 파일 저장 및 상태 PENDING 변경
            boolean success = userService.signupLawyer(loginUser, licenseFile);
            if (!success) throw new Exception("신청 처리 중 오류 발생");

            return "redirect:/mypage/lawyer/profile?msg=reapplySuccess";
        } catch (Exception e) {
            model.addAttribute("error", "신청 실패: " + e.getMessage());
            model.addAttribute("user", userService.getUserById(loginUser.getUserId()));
            return "mypage/lawyer/profile";
        }
    }
}