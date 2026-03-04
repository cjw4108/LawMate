package com.lawmate.Chatting;

import java.util.List;
import com.lawmate.dto.ChatMessage;
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
    public String aiConsult(@RequestParam(required = false, defaultValue = "GUEST") String userId, Model model) {

        // 🔥 수정: AI 상담방도 DB에 자동 생성하여 외래 키 에러 방지
        // AI 상담이므로 lawyerId 자리에 "GEMINI_AI"를 넣습니다.
        String roomId = chattingService.getOrCreateRoom(userId, "GEMINI_AI");

        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", userId);
        model.addAttribute("userType", "USER");
        model.addAttribute("chatWith", "AI"); // AI 모드 구분자 추가
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "ai/aiConsult";
    }

    /**
     * 변호사 1:1 상담 진입
     */
    @GetMapping("/direct/consult")
    public String directConsult(@RequestParam String lawyerId,
                                @RequestParam String userId,
                                Model model) {

        if (lawyerId == null || userId == null) {
            return "redirect:/lawyer/list";
        }

        // 1. Service를 통해 DB에서 방 생성 (자동 INSERT 로직 포함됨)
        String roomId = chattingService.getOrCreateRoom(userId, lawyerId);

        // 2. 기존 대화 내역 불러오기
        List<ChatMessage> history = chattingService.getChatHistory(roomId);
        model.addAttribute("chatHistory", history);

        // 3. 뷰에 전달할 데이터 세팅
        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", userId);
        model.addAttribute("lawyerId", lawyerId);
        model.addAttribute("chatWith", "LAWYER");
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "chat/directConsult";
    }
}