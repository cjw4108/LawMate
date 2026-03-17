package com.lawmate.Chatting;

import java.util.List;
import java.util.Map;

import com.lawmate.dto.ChatMessage;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class A02_ChattingController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(A02_ChattingController.class);
    @Autowired
    private A03_ChattingService chattingService;

    /**
     * [사용자용] AI 상담 진입
     */
    @GetMapping("/ai/consult")
    public String aiConsult(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        String currentUserId = loginUser.getUserId();

        // 이 부분을 간단히 고정합니다.
        String aiTargetId = "GEMINI_AI";

        String roomId = chattingService.getOrCreateRoom(currentUserId, aiTargetId);
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
     * [사용자용] 변호사 1:1 상담 신청 및 진입
     */
    @RequestMapping("/direct/consult")
    public String directConsult(
            @RequestParam(value = "lawyerId", required = false) String lawyerId,
            @RequestParam(value = "roomId", required = false) String roomId, // 👈 roomId 파라미터 추가
            HttpSession session,
            Model model) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // lawyerId가 아예 없는 경우 방어 로직
        if ((lawyerId == null || lawyerId.isEmpty()) && (roomId == null || roomId.isEmpty())) {
            System.out.println("DEBUG: 필요한 정보(lawyerId 또는 roomId)가 전달되지 않았습니다.");
            return "redirect:/lawyer/list";
        }

        String currentUserId = loginUser.getUserId();
        String finalRoomId = roomId;

        // 1. roomId가 넘어오지 않은 경우 (의뢰인이 변호사 목록에서 신규/기존 상담 클릭 시)
        if (finalRoomId == null || finalRoomId.isEmpty()) {
            finalRoomId = chattingService.getOrCreateRoom(currentUserId, lawyerId);
        }

        // 2. 대화 내역 가져오기
        List<ChatMessage> chatHistory = chattingService.selectChatHistory(finalRoomId);

        // 3. 모델 데이터 세팅
        model.addAttribute("roomId", finalRoomId);
        model.addAttribute("userId", currentUserId);
        model.addAttribute("lawyerId", lawyerId);
        model.addAttribute("chatHistory", chatHistory);
        model.addAttribute("chatWith", "LAWYER");

        // 유저 타입 판별 (상담 리스트의 lawyerId와 로그인한 유저의 이메일 앞부분 비교)
        // 변호사 본인이 들어가는 것인지 확인하여 UI 처리용 userType 전달
        String emailId = (loginUser.getEmail() != null) ? loginUser.getEmail().split("@")[0] : "";
        if (emailId.equals(lawyerId)) {
            model.addAttribute("userType", "LAWYER"); // 변호사 본인 입장
        } else {
            model.addAttribute("userType", "USER");   // 일반 의뢰인 입장
        }

        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "chat/directConsult";
    }

    /**
     * [변호사용] 상담 수락 처리 (Vue 대시보드 버튼)
     */
    @PostMapping("/direct/accept")
    @ResponseBody
    public ResponseEntity<String> acceptChat(@RequestParam("roomId") String roomId, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        // 세션이 없으면 권한 에러 (보안 강화)
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 로그인한 변호사 본인의 ID를 전달
        boolean isSuccess = chattingService.acceptConsultation(roomId, loginUser.getUserId());

        if (isSuccess) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }

    /**
     * [변호사용] 본인에게 온 상담 리스트 가져오기 (Vue 대시보드용)
     */
    @GetMapping("/api/chat/list")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLawyerChatList(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // 방법 A: userId 필드 자체가 "lawyer888"인 경우 (로그에 찍힌 값 활용)
        String lawyerId = loginUser.getUserId();

        // 방법 B (기존 유지 시): 이메일이 없는 경우를 대비한 방어 로직
        if (lawyerId == null || lawyerId.isEmpty()) {
            String email = loginUser.getEmail();
            if (email != null && email.contains("@")) {
                lawyerId = email.split("@")[0];
            }
        }

        System.out.println("🔍 [최종 확인] 대시보드 조회 ID: [" + lawyerId + "]");

        List<Map<String, Object>> list = chattingService.getLawyerChatList(lawyerId);
        return ResponseEntity.ok(list);
    }

    /**
     * [공통] 상담 신청 경로 매핑
     */
    @GetMapping("/consult/apply")
    public String applyConsultation(@RequestParam("lawyerId") String lawyerId, HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // 기존 1:1 상담 로직으로 포워딩
        return directConsult(lawyerId, null, session, model);
    }

    @PostMapping("/direct/close")
    @ResponseBody
    public String closeChat(@RequestParam String roomId, @RequestParam String lawyerId) {
        try {
            chattingService.closeChat(roomId, lawyerId);
            return "success";
        } catch (Exception e) {
            log.error("상담 종료 중 오류 발생: ", e);
            return "error";
        }
    }
}