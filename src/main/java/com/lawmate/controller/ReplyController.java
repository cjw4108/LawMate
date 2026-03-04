package com.lawmate.controller;

import com.lawmate.dto.Reply;
import com.lawmate.service.ReplyService;
import com.lawmate.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/write")
    public String writeReply(Reply reply, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) return "fail_auth";

        String role = loginUser.getRole();
        String lawyerStatus = loginUser.getLawyerStatus();

        if (!"ROLE_ADMIN".equals(role) &&
                !("ROLE_LAWYER".equals(role) && "APPROVED".equals(lawyerStatus))) {
            return "fail_auth";
        }

        reply.setUserId(loginUser.getUserId());
        replyService.saveReply(reply);
        return "success";
    }

    @PostMapping("/delete")
    public String deleteReply(@RequestParam("id") Long id, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "fail_auth";
        }

        replyService.deleteReply(id);
        return "success";
    }

    // ✅ 추가 - qnaId로 답변 전체 삭제
    @PostMapping("/deleteByQna")
    public String deleteReplyByQna(@RequestParam("qnaId") Long qnaId, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "fail_auth";
        }

        replyService.deleteByQnaId(qnaId);
        return "success";
    }
}