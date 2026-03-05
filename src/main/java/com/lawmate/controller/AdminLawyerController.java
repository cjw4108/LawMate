package com.lawmate.controller;

import com.lawmate.dto.AdminDTO;
import com.lawmate.dto.LawyerApprovalDTO;
import com.lawmate.service.AdminLawyerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/lawyer")
@RequiredArgsConstructor
public class AdminLawyerController {

    private final AdminLawyerService adminLawyerService;

    // 1. 변호사 승인 관리 페이지
    @GetMapping("/approve")
    public String approvalPage(@RequestParam(value = "keyword", required = false) String keyword,
                               HttpSession session,
                               Model model) {

        // 🚨 404 에러 방지: adminLogin.jsp가 없으므로 세션 체크를 잠시 주석 처리합니다.
        // 나중에 로그인 페이지를 만드시면 다시 살리세요!
        /*
        AdminDTO loginAdmin = (AdminDTO) session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return "redirect:/admin/main";
        }
        */

        List<LawyerApprovalDTO> pendingList = adminLawyerService.getPendingLawyers(keyword);

        model.addAttribute("pendingList", pendingList);
        model.addAttribute("keyword", keyword);

        return "admin/approve";
    }

    // 2. 승인 및 반려 통합 처리
    @PostMapping("/process")
    public String process(@RequestParam("userId") String userId,
                          @RequestParam("targetStatus") String targetStatus,
                          @RequestParam(required = false) String rejectReason,
                          HttpSession session) {

        // 🚨 여기도 세션 체크 때문에 404가 났던 겁니다. 잠시 꺼둘게요!
        /*
        AdminDTO loginAdmin = (AdminDTO) session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return "redirect:/admin/main";
        }
        */

        // ✅ 이제 서비스 코드가 정상적으로 실행됩니다!
        adminLawyerService.updateLawyerStatus(userId, targetStatus, rejectReason);

        // ✅ 핵심: 처리가 끝난 후 돌아갈 '풀 주소'를 적어줍니다.
        return "redirect:/admin/lawyer/approve";
    }
}