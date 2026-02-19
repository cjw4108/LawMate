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

    public QnaController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", questionService.findAll());
        return "qna/qnaList"; // 파일명 qnaList.jsp와 일치 [cite: 11]
    }

    @GetMapping("/write") // JSP의 href="/qna/write"와 대응
    public String writeForm() {
        return "qna/qnaRegister"; // 파일명 qnaRegister.jsp와 일치 [cite: 1]
    }

    @PostMapping("/write") // JSP의 action="/qna/write"와 대응
    public String write(Question question) {
        questionService.save(question);
        return "redirect:/qna/list"; // 등록 후 리스트로 이동
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionService.findById(id));
        return "qna/qnaDetail"; // 파일명 qnaDetail.jsp와 일치 [cite: 5]
    }
}