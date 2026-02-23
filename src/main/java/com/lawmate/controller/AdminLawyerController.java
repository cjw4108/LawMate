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

    @GetMapping("/approval")
    public String approvalPage(Model model) {
        model.addAttribute("lawyers", adminLawyerService.getPendingLawyers());
        return "admin/lawyerApproval";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam String lawyerId) {
        adminLawyerService.updateLawyerStatus(lawyerId, "APPROVED");
        return "redirect:/admin/lawyer/approval";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam String lawyerId) {
        adminLawyerService.updateLawyerStatus(lawyerId, "REJECTED");
        return "redirect:/admin/lawyer/approval";
    }
}
