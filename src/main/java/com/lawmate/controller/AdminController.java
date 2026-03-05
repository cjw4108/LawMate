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
    private final AdminLawyerService adminLawyerService;

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
    @GetMapping("/approve")
    public String approvePage(@RequestParam(value = "keyword", required = false) String keyword,
                              Model model) {

        // 서비스의 메서드 구조(String keyword 필요)에 맞춰서 호출!
        List<LawyerApprovalDTO> pendingList = adminLawyerService.getPendingLawyers(keyword);

        model.addAttribute("pendingList", pendingList);
        model.addAttribute("keyword", keyword); // 검색창에 검색어 유지용

        return "admin/approve";
    }
}