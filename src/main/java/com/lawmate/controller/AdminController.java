package com.lawmate.controller;

import com.lawmate.dto.Question;
import com.lawmate.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 서비스 주입을 위해 추가
public class AdminController {

    private final QuestionService questionService; // 서비스 연결

    @GetMapping("/main")
    public String adminMain() {
        return "admin/adminMain";
    }

    // 게시판 운영 관리 (신고 리스트 출력)
    @GetMapping("/qna")
    public String adminQna(Model model) {
        // 실제 신고된 데이터를 가져와서 모델에 담습니다.
        List<Question> reportedList = questionService.findReportedQuestions();
        model.addAttribute("reportedList", reportedList);

        // 기존 return 경로가 admin/adminQna라면 그대로 유지
        return "admin/adminQna";
    }

    @GetMapping("/users")
    public String adminUsers() {
        return "admin/adminUsers";
    }

    @GetMapping("/approve")
    public String adminApprove() {
        return "admin/adminApprove";
    }
}