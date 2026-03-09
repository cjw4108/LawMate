package com.lawmate.controller;

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

    @GetMapping("/approve")
    public String approvalPage(@RequestParam(value = "role", required = false) String role,
                               @RequestParam(value = "status", required = false) String status,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {

        // 서비스에 3개 파라미터를 모두 전달합니다.
        List<LawyerApprovalDTO> pendingList = adminLawyerService.getPendingLawyers(role, status, keyword);

        model.addAttribute("pendingList", pendingList);
        // 검색 후에도 드롭다운과 검색어가 유지되도록 다시 보냅니다.
        model.addAttribute("role", role);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

        return "admin/approve";
    }

    @PostMapping("/process")
    public String process(@RequestParam("userId") String userId,
                          @RequestParam("targetStatus") String targetStatus,
                          @RequestParam(required = false) String rejectReason) {

        adminLawyerService.updateLawyerStatus(userId, targetStatus, rejectReason);
        return "redirect:/admin/lawyer/approve";
    }
}