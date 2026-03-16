package com.lawmate.controller;

import com.lawmate.dto.Reply;
import com.lawmate.service.ReplyService;
import com.lawmate.dto.UserDTO;
import com.lawmate.entity.ReplyEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 기본적인 접근 권한 (로그인 여부 및 전문가/관리자 확인)
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
        if (loginUser == null) return "fail_auth";

        ReplyEntity reply = replyService.getReplyEntity(id);
        if (reply == null) return "fail";

        boolean isAdmin = "ROLE_ADMIN".equals(loginUser.getRole());

        // [수정 포인트] 아이디 비교 시 equals()를 사용하여 타입 불일치 및 Null 안전성 확보
        // 만약 loginUser.getUserId()가 String이고 reply.getUserId()가 String이라면 equals가 답입니다.
        boolean isAuthor = Objects.equals(loginUser.getUserId(), reply.getUserId());

        boolean isApprovedLawyer = "ROLE_LAWYER".equals(loginUser.getRole()) && "APPROVED".equals(loginUser.getLawyerStatus());

        // 로직: 관리자이거나, (승인된 변호사인데 본인 글인 경우)
        if (isAdmin || (isApprovedLawyer && isAuthor)) {
            replyService.updateReply(id, content, loginUser.getUserId());
            return "success";
        }

        return "fail_auth";
    }

    @PostMapping("/delete")
    public String deleteReply(@RequestParam("id") Long id, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "fail_auth";

        ReplyEntity reply = replyService.getReplyEntity(id);
        if (reply == null) return "fail";

        boolean isAdmin = "ROLE_ADMIN".equals(loginUser.getRole());
        boolean isAuthor = Objects.equals(loginUser.getUserId(), reply.getUserId());

        // 관리자라면 무조건 삭제 가능, 변호사는 본인 글만 삭제 가능
        if (isAdmin || isAuthor) {
            replyService.deleteReply(id);
            return "success";
        }

        return "fail_auth";
    }

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