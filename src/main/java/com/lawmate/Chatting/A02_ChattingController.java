package com.lawmate.Chatting;

import java.util.List;
import com.lawmate.dto.ChatMessage;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class A02_ChattingController {

    @Autowired
    private A03_ChattingService chattingService;

    /**
     * AI 상담 진입
     */
    @GetMapping("/ai/consult")
    public String aiConsult(HttpSession session, Model model) {

        // 1. [핵심] 세션에서 로그인 유저 정보 확인
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        // 2. 로그인되지 않은 경우 (방 개설 안 함)
        if (loginUser == null) {
            System.out.println("⚠️ 비로그인 사용자의 접근 - 로그인 페이지로 이동");
            // 로그인 페이지 경로가 /login 이 맞는지 확인하세요.
            return "redirect:/login";
        }

        // 3. 로그인된 경우에만 아래 로직 실행 (방 조회 및 생성)
        String currentUserId = loginUser.getUserId();
        String aiTargetId = "GEMINI_AI";
        String chatWith = "AI";

        // 기존 방이 있으면 가져오고, 없으면 이때만 생성됨
        String roomId = chattingService.getOrCreateRoom(currentUserId, aiTargetId, chatWith);

        // 기존 대화 내역 조회
        List<ChatMessage> chatHistory = chattingService.selectChatHistory(roomId);

        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", currentUserId);
        model.addAttribute("chatHistory", chatHistory);
        model.addAttribute("userType", "USER");
        model.addAttribute("chatWith", "AI");
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "ai/aiConsult";
    }

    /**
     * 변호사 1:1 상담 진입
     */
    // http://localhost:8080/direct/consult?lawyerId=lawtest123&userId=testuser123
    @RequestMapping("/direct/consult")
    public String directConsult(
            @RequestParam("lawyerId") String lawyerId,
            @RequestParam(value = "userId", required = false) String userId,
            HttpSession session,
            Model model) {

        // 1. 세션 체크 (로그인 안 되어 있으면 로그인 페이지로)
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        String currentUserId = loginUser.getUserId();
        String chatWith = "LAWYER"; // 변호사 상담 모드

        // 2. [변경] 고정된 ROOM_ID 대신 DB에서 방을 찾거나 생성합니다.
        // getOrCreateRoom 내부에서 USER_ID와 LAWYER_ID 조합으로 기존 방을 조회합니다.
        String roomId = chattingService.getOrCreateRoom(currentUserId, lawyerId, chatWith);

        // 3. 기존 대화 내역 조회 (이게 있어야 이전 대화가 보입니다)
        List<ChatMessage> chatHistory = chattingService.selectChatHistory(roomId);

        // 4. JSP로 데이터 전달
        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", currentUserId);
        model.addAttribute("lawyerId", lawyerId);
        model.addAttribute("chatHistory", chatHistory); // 대화 내역 추가
        model.addAttribute("chatWith", chatWith);
        model.addAttribute("userType", "USER");
        model.addAttribute("socketServer", "ws://localhost:8080/chatHandler");

        return "chat/directConsult";
    }
}