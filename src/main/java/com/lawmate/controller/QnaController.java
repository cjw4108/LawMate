package com.lawmate.controller;

import com.lawmate.dto.Question;
import com.lawmate.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/qna")
public class QnaController {

    private final QuestionService questionService;

    public QnaController(QuestionService questionService) {
        this.questionService = questionService;
    }

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

    @GetMapping("/write")
    public String writeForm() { return "qna/qnaRegister"; }

    @PostMapping("/write")
    public String write(Question question) {
        questionService.save(question);
        return "redirect:/qna/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Question question = questionService.findById(id);

        if (question == null) return "redirect:/qna/list";

        model.addAttribute("question", question);

        boolean isFavorite = false;
        if (userId != null) {
            isFavorite = questionService.isFavorite(id, userId);
        }
        model.addAttribute("isFavorite", isFavorite);

        return "qna/qnaDetail";
    }

    @PostMapping("/report/{id}")
    @ResponseBody
    public String report(@PathVariable Long id, @RequestParam("reason") String reason, HttpSession session) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) userId = "anonymous"; // 로그인 미구현 시 임시값

            questionService.report(id, reason, userId);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/favorite/{id}")
    @ResponseBody
    public String toggleFavorite(@PathVariable Long id, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "login_required";

        boolean isAdded = questionService.toggleFavorite(id, userId);
        return isAdded ? "added" : "removed";
    }

    @GetMapping("/admin/list")
    public String adminList(Model model) {
        model.addAttribute("reportedList", questionService.findReportedQuestions());
        return "qna/adminQna";
    }
}