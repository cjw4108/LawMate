package com.lawmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/qna")
public class QnaController {

    @GetMapping("/list")
    public String qnaList() {
        return "qna/qnaList"; // /WEB-INF/views/qna/qnaList.jsp
    }

    @GetMapping("/register")
    public String qnaRegisterForm() {
        return "qna/qnaRegister"; // /WEB-INF/views/qna/qnaRegister.jsp
    }

    @PostMapping("/insert")
    public String qnaInsert() {
        // TODO: Service를 통한 DB 저장 로직 구현 (Vo/Dto 사용)
        return "redirect:/qna/list";
    }

    @GetMapping("/detail")
    public String qnaDetail() {
        return "qna/qnaDetail"; // /WEB-INF/views/qna/qnaDetail.jsp
    }

    @PostMapping("/answer/insert")
    public String answerInsert() {
        // TODO: 답변 저장 로직
        return "redirect:/qna/detail";
    }
}