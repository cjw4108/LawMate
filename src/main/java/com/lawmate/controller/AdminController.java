package com.lawmate.controller;

import com.lawmate.dto.QuestionListDTO;
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

    // ============================
    // 관리자 메인 대시보드
    // ============================
    @GetMapping("/main")
    public String adminMain(Model model) {
        int unansweredCount = questionService.getUnansweredCount();
        int reportedCount = questionService.getReportedCount();

        model.addAttribute("unansweredCount", unansweredCount);
        model.addAttribute("reportedCount", reportedCount);

        return "admin/adminMain";
    }

    // ============================
    // QnA 통합 관리 페이지
    // ============================
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

    // ============================
    // 🔥 QnA 삭제 및 복구
    // ============================
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

    // ============================
    // 사용자 관리 페이지
    // ============================
    @GetMapping("/users")
    public String adminUsers() {
        return "admin/adminUsers";
    }

    // 🔴 [수정 완료]
    // 여기서 충돌나던 lawyer/approve 와 lawyer/process 메서드를 삭제했습니다.
    // 해당 기능은 이미 'AdminLawyerController'가 담당하고 있으니 안심하세요!
}