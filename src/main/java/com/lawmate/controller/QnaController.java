package com.lawmate.controller;

import com.lawmate.dto.Question;
import com.lawmate.dto.UserDTO;
import com.lawmate.service.QuestionService;
import com.lawmate.entity.ReplyEntity;
import com.lawmate.dto.QuestionListDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QuestionService questionService;

    // 1. 질문 목록 (검색 및 정렬 통합)
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "latest") String sort,
                       Model model,
                       HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = (loginUser != null) ? loginUser.getUserId() : null;

        if ("favorite".equals(sort) && userId == null) {
            return "redirect:/login";
        }

        List<QuestionListDTO> list =
                questionService.getList(keyword, sort, userId);

        model.addAttribute("list", list);
        model.addAttribute("currentSort", sort);
        model.addAttribute("keyword", keyword);

        return "qna/qnaList";
    }

    // 2. 질문 상세 보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(required = false) String sort,
                         @RequestParam(required = false) String keyword,
                         Model model,
                         HttpSession session) {

        Question question = questionService.findById(id);
        if (question == null) {
            return "redirect:/qna/list";
        }

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        boolean isFavorite = false;
        if (loginUser != null) {
            isFavorite = questionService.isFavorite(id, loginUser.getUserId());
        }

        question.setFavoriteCount(questionService.getFavoriteCount(id));

        List<ReplyEntity> replies = questionService.getReplies(id);
        int replyCount = questionService.getReplyCount(id);

        model.addAttribute("question", question);
        model.addAttribute("replies", replies);
        model.addAttribute("replyCount", replyCount);
        model.addAttribute("isFavorite", isFavorite);

        // 🔥 추가
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);

        return "qna/qnaDetail";
    }

    // 3. 찜하기 토글 (Ajax)
    @PostMapping("/favorite/{id}")
    @ResponseBody
    public Map<String, Object> toggleFavorite(@PathVariable Long id,
                                              HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        Map<String, Object> result = new HashMap<>();

        if (loginUser == null) {
            result.put("status", "login_required");
            return result;
        }

        String status = questionService.toggleFavorite(id, loginUser.getUserId());
        int updatedCount = questionService.getFavoriteCount(id);

        result.put("status", status);
        result.put("count", updatedCount);

        return result;
    }

    // 4. 답변 등록
    @PostMapping("/reply/{id}")
    public String registerReply(@PathVariable Long id,
                                @RequestParam String content,
                                HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        questionService.registerReply(id, content, loginUser.getUserId());

        return "redirect:/qna/detail/" + id;
    }

    // 5. 신고 처리
    @PostMapping("/report/{id}")
    @ResponseBody
    public String report(@PathVariable Long id,
                         HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "login_required";
        }

        try {
            questionService.report(id, "일반 신고", loginUser.getUserId());
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // 6. 게시글 작성 폼
    @GetMapping("/write")
    public String writeForm(HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        return "qna/qnaWrite";
    }

    // 7. 게시글 작성 처리
    @PostMapping("/write")
    public String write(Question question, HttpSession session) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        question.setUserId(loginUser.getUserId());
        questionService.save(question);

        return "redirect:/qna/list";
    }


}