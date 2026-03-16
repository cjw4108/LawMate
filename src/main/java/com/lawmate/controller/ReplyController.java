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


    private boolean hasAuthority(UserDTO loginUser) {
        if (loginUser == null) return false;
        String role = loginUser.getRole();
        String lawyerStatus = loginUser.getLawyerStatus();
        return "ROLE_ADMIN".equals(role) ||
                ("ROLE_LAWYER".equals(role) && "APPROVED".equals(lawyerStatus));
    }

    @PostMapping("/write")
    public String writeReply(Reply reply, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (!hasAuthority(loginUser)) return "fail_auth";

        reply.setUserId(loginUser.getUserId());
        replyService.saveReply(reply);
        return "success";
    }


    @PostMapping("/update")
    public String updateReply(@RequestParam("id") Long id,
                              @RequestParam("content") String content,
                              HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (!hasAuthority(loginUser)) return "fail_auth";

        replyService.updateReply(id, content, loginUser.getUserId());
        return "success";
    }


    @PostMapping("/delete")
    public String deleteReply(@RequestParam("id") Long id, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (!hasAuthority(loginUser)) return "fail_auth";

        replyService.deleteReply(id);
        return "success";
    }

    @PostMapping("/deleteByQna")
    public String deleteReplyByQna(@RequestParam("qnaId") Long qnaId, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (!hasAuthority(loginUser)) return "fail_auth";

        replyService.deleteByQnaId(qnaId);
        return "success";
    }
}