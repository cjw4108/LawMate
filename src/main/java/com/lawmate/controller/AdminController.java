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

    // 관리자 메인 대시보드
    @GetMapping("/main")
    public String adminMain(Model model) {
        model.addAttribute("unansweredCount", questionService.getUnansweredCount());
        model.addAttribute("reportedCount", questionService.getReportedCount());
        return "admin/adminMain";
    }

    // QnA 통합 관리 페이지
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

    // ⭐ 신고 사유 가져오기 API (비동기 JSON 반환)
    @GetMapping("/qna/reports/{id}")
    @ResponseBody
    public List<Object[]> getReportReasons(@PathVariable("id") Long id) {
        return questionService.getReportReasonsByQnaId(id);
    }

    // 소프트 삭제
    @PostMapping("/qna/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id,
                                 @RequestParam(value = "filter", defaultValue = "all") String filter,
                                 @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        questionService.softDelete(id);
        return "redirect:/admin/qna?filter=" + filter + "&sort=" + sort;
    }

    // 복구
    @PostMapping("/qna/restore/{id}")
    public String restoreQuestion(@PathVariable("id") Long id,
                                  @RequestParam(value = "filter", defaultValue = "all") String filter,
                                  @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        questionService.restore(id);
        return "redirect:/admin/qna?filter=" + filter + "&sort=" + sort;
    }

    @GetMapping("approve") // JSP의 버튼 주소와 일치시킴
    public String approvePage(Model model) {
        // 1. 여기서 승인 대기 중인 변호사 목록을 가져와야 합니다.
        // List<UserDTO> lawyerList = userService.getPendingLawyers();
        // model.addAttribute("lawyerList", lawyerList);

        // 2. 리턴값은 반드시 '폴더명/파일명'이어야 합니다.
        return "admin/approve"; // src/main/webapp/WEB-INF/views/admin/approve.jsp
    }
}