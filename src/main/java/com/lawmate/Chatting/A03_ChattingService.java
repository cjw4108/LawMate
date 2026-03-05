package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.client.HttpClientErrorException;
@Service
public class A03_ChattingService {

    private final ChatMapper chatMapper;

    @Value("${gemini.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public A03_ChattingService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    /**
     * 1. 방 생성 또는 기존 방 조회 (자동 생성 로직 추가)
     */
    @Transactional
    public String getOrCreateRoom(String userId, String lawyerId) {
        // DB에서 이 사용자(userId)와 AI(lawyerId)의 조합으로 된 방이 있는지 확인
        String existingRoomId = chatMapper.findRoomIdByParticipants(userId, lawyerId);

        if (existingRoomId != null) {
            // 이미 있으면 그 방 ID 반환 (내역 유지)
            return existingRoomId;
        }

        // 없으면 해당 사용자만을 위한 새 방 생성
        String newRoomId = UUID.randomUUID().toString();
        chatMapper.insertChatRoom(newRoomId, userId, lawyerId);
        return newRoomId;
    }

    /**
     * 2. 대화 기록 가져오기
     */
    public List<ChatMessage> getChatHistory(String roomId) {
        System.out.println(roomId + " 방의 대화 내역을 조회합니다.");
        List<ChatMessage> history = chatMapper.selectChatHistory(roomId);
        System.out.println("🔍 [디버깅] 방ID: " + roomId + " | 조회된 메시지 수: " + (history != null ? history.size() : 0));
        return chatMapper.selectChatHistory(roomId);
    }

    /**
     * 3. 메시지 DB 저장 (방 존재 여부 체크 로직 추가)
     */
    @Transactional
    public void saveMessage(String roomId, String senderId, String senderType, String content) {
        // 🛡️ 방어 코드: 만약 방이 DB에 없다면 생성해줍니다.
        if (chatMapper.checkRoomExists(roomId) == 0) {
            System.out.println("⚠️ 방이 없어 자동 생성 시도: " + roomId);
            chatMapper.insertChatRoom(roomId, senderId, "SYSTEM_AUTO");
        }

        ChatMessage dto = new ChatMessage();
        dto.setRoomId(roomId);
        dto.setSenderType(senderType != null ? senderType : "USER");
        dto.setMessage(content);

        chatMapper.insertMessage(dto);
        System.out.println("💾 DB 저장 완료: [방:" + roomId + "] " + content);
    }

    /**
     * 4. Gemini AI 상담 호출
     */
    public String askGemini(String userMessage) {
        // 엔드포인트는 잘 작성되었습니다.
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textPart = Map.of("text", userMessage);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(parts));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            System.out.println("🚀 Gemini API 호출 시도: gemini-3.1-flash-lite");
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            // 응답 파싱 로직 (생략)
            List candidates = (List) response.getBody().get("candidates");
            Map first = (Map) candidates.get(0);
            Map content = (Map) first.get("content");
            List partList = (List) content.get("parts");
            Map textMap = (Map) partList.get(0);

            return textMap.get("text").toString();

        } catch (HttpClientErrorException e) { // NotFound 대신 상위 클래스인 HttpClientErrorException 권장
            // 💡 여기서 로그를 찍어보면 실제 구글이 보내는 정확한 에러 메시지를 볼 수 있습니다.
            System.err.println("❌ API 에러 발생: " + e.getResponseBodyAsString());
            return "AI 상담 연결에 실패했습니다 (모델 확인 필요).";
        } catch (Exception e) {
            System.err.println("❌ 일반 에러: " + e.getMessage());
            return "상담사와 연결할 수 없습니다.";
        }
    }

    /**
     * 5. 방 상태 변경
     */
    @Transactional
    public void switchToLawyerMode(String roomId) {
        // chatMapper에 updateRoomStatus 같은 메서드를 만들어 연동하세요.
        System.out.println(roomId + " 방이 변호사 상담 모드로 전환되었습니다.");
    }
    public void saveMessageAsync(String roomId, String senderId, String senderType, String content) {
        // DB 저장을 별도 쓰레드에서 수행하여 사용자 대기 시간 감소
        CompletableFuture.runAsync(() -> {
            saveMessage(roomId, senderId, senderType, content);
        });
    }
}