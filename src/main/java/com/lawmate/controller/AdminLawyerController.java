package com.lawmate.controller;

import com.lawmate.dto.UserDTO;
import com.lawmate.service.AdminLawyerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/lawyer")
public class AdminLawyerController {

    private final AdminLawyerService adminLawyerService;

    public AdminLawyerController(AdminLawyerService adminLawyerService) {
        this.adminLawyerService = adminLawyerService;
    }

    // 1. 변호사 승인 관리 페이지 (목록 조회)
    @GetMapping("/approval")
    public String approvalPage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        // 관리자 권한 체크 [설계서 LOGIN-001 반영]
        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("lawyers", adminLawyerService.getPendingLawyers());
        model.addAttribute("allLawyers", adminLawyerService.getAllLawyers());
        return "admin/lawyerApproval"; // src/main/webapp/WEB-INF/views/admin/lawyerApproval.jsp
    }

    // 2. 변호사 상세 정보 및 증빙 서류 확인
    @GetMapping("/detail/{lawyerId}")
    public String detail(@PathVariable String lawyerId, HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        // 변호사 정보 및 자격 증빙 파일 경로 확인용 데이터 전달
        model.addAttribute("lawyer", adminLawyerService.getLawyerDetail(lawyerId));
        return "admin/lawyerDetail";
    }

    // 3. 변호사 승인 처리
    @PostMapping("/approve")
    public String approve(@RequestParam String lawyerId, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        // 상태를 'APPROVED'로 변경 [설계서 JOIN-002 연계]
        adminLawyerService.updateLawyerStatus(lawyerId, "APPROVED", null);
        return "redirect:/admin/lawyer/approval";
    }

    // 4. 변호사 반려 처리
    @PostMapping("/reject")
    public String reject(@RequestParam String lawyerId,
                         @RequestParam(required = false) String rejectReason,
                         HttpSession session,
                         Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        // 반려 사유 필수 체크
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            model.addAttribute("error", "반려 사유를 반드시 입력해야 합니다.");
            model.addAttribute("lawyers", adminLawyerService.getPendingLawyers());
            return "admin/lawyerApproval";
        }

        // 상태를 'REJECTED'로 변경하고 사유 저장
        adminLawyerService.updateLawyerStatus(lawyerId, "REJECTED", rejectReason);
        return "redirect:/admin/lawyer/approval";
    }
}
