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

    // 1. 변호사 승인 관리 페이지 (목록 조회)
    @GetMapping("/approve")
    public String approvalPage(HttpSession session, Model model) {
        AdminDTO loginAdmin = (AdminDTO) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            return "redirect:/admin/login";
        }

        // ✅ 수정: static 메서드 대신 인스턴스 메서드 호출
        List<LawyerApprovalDTO> pendingList = adminLawyerService.getPendingLawyers();
        model.addAttribute("pendingList", pendingList);

        return "admin/approve";
    }

    // 2. 승인 및 반려 통합 처리
    @PostMapping("/process")
    public String process(@RequestParam String userId,
                          @RequestParam String targetStatus,
                          @RequestParam(required = false) String rejectReason,
                          HttpSession session) {

        AdminDTO loginAdmin = (AdminDTO) session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return "redirect:/admin/login";
        }

        adminLawyerService.updateLawyerStatus(userId, targetStatus, rejectReason);

        return "redirect:/admin/lawyer/approve";
    }
}
