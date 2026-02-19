package com.lawmate.controller;

import com.lawmate.dto.Question;
import com.lawmate.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QuestionService questionService;

    // 질문 목록
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "latest") String sort,
                       Model model, HttpSession session) {
        List<Question> list;
        String userId = (String) session.getAttribute("userId");

        if (keyword != null && !keyword.isEmpty()) {
            list = questionService.search(keyword);
        } else {
            switch (sort) {
                case "replies":  list = questionService.findAllByOrderByAnsweredDesc(); break;
                case "likes":    list = questionService.findAllByOrderByLikesDesc(); break;
                case "favorite":
                    if (userId == null) return "redirect:/login";
                    list = questionService.findMyFavorites(userId);
                    break;
                default:         list = questionService.findAllByOrderByCreatedAtDesc(); break;
            }
        }
        model.addAttribute("list", list);
        model.addAttribute("currentSort", sort);
        return "qna/qnaList";
    }

    // 질문 상세
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Question question = questionService.findById(id);
        if (question == null) return "redirect:/qna/list";
        model.addAttribute("question", question);
        return "qna/qnaDetail";
    }

    // 신고 처리 (Ajax)
    @PostMapping("/report/{id}")
    @ResponseBody
    public String report(@PathVariable Long id, @RequestParam("reason") String reason, HttpSession session) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) userId = "anonymous";
            questionService.report(id, reason, userId);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }
}