package com.lawmate.Chatting;

import java.util.List;
import com.lawmate.dto.ChatMessage;
import com.lawmate.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class A02_ChattingController {

    @Autowired
    private A03_ChattingService chattingService;

    /**
     * AI 상담 진입
     */
    @GetMapping("/ai/consult")
    public String aiConsult(HttpSession session, Model model) {
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        // (프로젝트에서 사용하는 세션 키값 "loginUser" 등을 확인하세요)
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId;

        if (loginUser != null) {
            userId = loginUser.getUserId(); // 로그인한 아이디 (예: testuser123)
        } else {
            // 로그인을 안 했다면 임시 ID 부여 (세션 ID 활용 등)
            userId = "GUEST_" + session.getId().substring(0, 8);
        }

        // 2. 해당 사용자 ID 전용 AI 상담방 조회 또는 생성
        // (Service에서 userId와 "GEMINI_AI" 조합으로 방을 찾아줍니다)
        String roomId = chattingService.getOrCreateRoom(userId, "GEMINI_AI");

        // 3. 해당 방의 대화 내역 불러오기
        List<ChatMessage> history = chattingService.getChatHistory(roomId);

        model.addAttribute("chatHistory", history);
        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", userId);
        model.addAttribute("userType", "USER");
        model.addAttribute("chatWith", "AI");
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "ai/aiConsult";
    }

    /**
     * 변호사 1:1 상담 진입
     */
    @GetMapping("/direct/consult")
    public String directConsult(@RequestParam String lawyerId,
                                @RequestParam String userId,
                                HttpSession session, // 세션 추가
                                Model model) {

        // 1. 로그인 체크 (세션에 로그인 정보가 없으면 리다이렉트)
        // 예: "loginMember"는 프로젝트에서 사용하는 세션 키값으로 변경하세요.
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/login"; // 로그인 페이지로 이동
        }

        if (lawyerId == null || userId == null) {
            return "redirect:/lawyer/list";
        }

        // 2. 기존 방을 찾거나 새로 생성 (고도화된 서비스 호출)
        String roomId = chattingService.getOrCreateRoom(userId, lawyerId);

        // 3. 대화 내역 불러오기 (기존 방일 경우 과거 내역이 나옴)
        List<ChatMessage> history = chattingService.getChatHistory(roomId);

        model.addAttribute("chatHistory", history);
        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", userId);
        model.addAttribute("lawyerId", lawyerId);
        model.addAttribute("chatWith", "LAWYER");
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "chat/directConsult";
    }
}