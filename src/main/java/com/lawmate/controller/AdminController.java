package com.lawmate.controller;

import com.lawmate.dto.LawyerApprovalDTO;
import com.lawmate.dto.QuestionListDTO;
import com.lawmate.service.AdminLawyerService;
import com.lawmate.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QuestionService questionService;
    private final AdminLawyerService adminLawyerService; // ✅ 추가

    @GetMapping("/main")
    public String adminMain(Model model) {
        model.addAttribute("unansweredCount", questionService.getUnansweredCount());
        model.addAttribute("reportedCount", questionService.getReportedCount());
        return "admin/adminMain";
    }

    @GetMapping("/qna")
    public String adminQna(
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Model model) {

        List<QuestionListDTO> list = questionService.getAdminQuestionList(filter, sort);
        model.addAttribute("qnaList", list);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("currentSort", sort);
        return "admin/adminQna";
    }

    @GetMapping("/qna/reports/{id}")
    @ResponseBody
    public List<Object[]> getReportReasons(@PathVariable("id") Long id) {
        return questionService.getReportReasonsByQnaId(id);
    }

    @PostMapping("/qna/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id,
                                 @RequestParam(value = "filter", defaultValue = "all") String filter,
                                 @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        questionService.softDelete(id);
        return "redirect:/admin/qna?filter=" + filter + "&sort=" + sort;
    }

    @PostMapping("/qna/restore/{id}")
    public String restoreQuestion(@PathVariable("id") Long id,
                                  @RequestParam(value = "filter", defaultValue = "all") String filter,
                                  @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        questionService.restore(id);
        return "redirect:/admin/qna?filter=" + filter + "&sort=" + sort;
    }

    // ✅ pendingList 조회 추가
    @GetMapping("approve")
    public String approvePage(Model model) {
        List<LawyerApprovalDTO> pendingList = adminLawyerService.getPendingLawyers();
        model.addAttribute("pendingList", pendingList);
        return "admin/approve";
    }
}