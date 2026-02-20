package com.lawmate.controller;

import com.lawmate.service.AdminLawyerService;
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

    /* 변호사 승인관리 화면 */
    @GetMapping("/approval")
    public String approvalPage(Model model) {
        model.addAttribute("lawyers",
                adminLawyerService.getPendingLawyers());
        return "admin/lawyerApproval";
    }

    /* 승인 */
    @PostMapping("/approve")
    public String approve(@RequestParam String lawyerId) {
        adminLawyerService.approveLawyer(lawyerId);
        return "redirect:/admin/lawyer/approval";
    }

    /* 반려 */
    @PostMapping("/reject")
    public String reject(@RequestParam String lawyerId) {
        adminLawyerService.rejectLawyer(lawyerId);
        return "redirect:/admin/lawyer/approval";
    }
}
