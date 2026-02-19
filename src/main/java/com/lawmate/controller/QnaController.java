package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.lawmate.dto.Question;
import com.lawmate.service.QuestionService;

@Controller
@RequestMapping("/qna")
public class QnaController {

    private final QuestionService questionService;

    // ✅ Lombok 대신 직접 생성자 작성
    public QnaController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", questionService.findAll());
        return "qna/list";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "qna/write";
    }

    @PostMapping("/write")
    public String write(Question question) {
        questionService.save(question);
        return "redirect:/qna/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionService.findById(id));
        return "qna/detail";
    }
}
