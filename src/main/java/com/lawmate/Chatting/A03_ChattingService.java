package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;


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
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(A03_ChattingService.class);

    public String getOrCreateRoom(String userId, String lawyerId) {
        // 1. 기존 방이 있는지 확인 (위에서 수정한 쿼리 덕분에 숫자/문자 둘 다 대응 가능)
        String roomId = chatMapper.findRoom(userId, lawyerId);

        if (roomId != null) {
            return roomId;
        }

        // 2. 방이 없을 경우 생성하기 전에 '진짜 이메일 아이디'를 한 번 조회해옵니다.
        // (만약 lawyerId가 '16' 같은 숫자라면 이메일 아이디로 변환하는 과정)
        String realLawyerId = chatMapper.getLawyerEmailId(lawyerId);

        roomId = UUID.randomUUID().toString();
        chatMapper.createChatRoom(roomId, userId, realLawyerId); // 반드시 이메일 아이디로 저장!

        return roomId;
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
    /**
     * 3. 메시지 DB 저장 (방 존재 여부 체크 로직 추가)
     */
    @Transactional
    public void saveMessage(String roomId, String senderId, String senderType, String content) {

        try {
            // 🔍 진입 로그
            System.out.println("======= [DB 저장 시도] =======");

            // [방어 로직 추가] 사용자가 텍스트 없이 파일만 보낸 경우 content가 null이거나 비어있을 수 있음
            // DB의 CONTENT 컬럼이 NOT NULL이므로 기본 문구를 설정해줍니다.
            if (content == null || content.trim().isEmpty()) {
                content = "(첨부파일 전송)";
            }

            System.out.println("방ID: " + roomId);
            System.out.println("발신ID: " + senderId);
            System.out.println("타입: " + senderType);
            System.out.println("내용: " + content);

            if (roomId == null || senderId == null) {
                System.err.println("⚠️ 필수 데이터(roomId 또는 senderId)가 null입니다!");
                return;
            }

            ChatMessage dto = new ChatMessage();
            dto.setRoomId(roomId);
            dto.setSenderId(senderId);
            dto.setSenderType(senderType);
            dto.setMessage(content); // content가 (첨부파일 전송)으로 치환되어 들어감

            chatMapper.insertMessage(dto);
            System.out.println("✅ [DB 저장 성공]");
            System.out.println("==============================");

        } catch (Exception e) {
            System.err.println("❌ [DB 저장 중 예외 발생]");
            e.printStackTrace();
        }
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
    // A03_ChattingService.java의 153라인 근처 수정
    public List<ChatMessage> selectChatHistory(String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        // 매퍼에 @Param("roomId")로 등록했으므로 roomId만 보냅니다.
        return chatMapper.selectChatHistory(roomId);
    }

    // A03_ChattingService.java 에 추가할 로직 예시

    public String requestAiAnalysis(MultipartFile file) {
        String pythonUrl = "http://localhost:8000/analyze";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // [핵심] 파이썬 서버가 '파일'임을 인식하게 하는 리소스 생성
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // 'file'이라는 키값은 파이썬의 analyze_file(file: UploadFile = File(...))과 일치해야 합니다.
            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Python 서버 호출
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                // 파이썬에서 return {"status": "success", "analysis": "..."} 라고 보낸 경우
                return result.get("analysis").toString();
            }
        } catch (Exception e) {
            // [디버깅용] 콘솔에 에러 내용을 상세히 찍어줍니다.
            System.err.println("❌ 파이썬 서버 통신 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return "파일 분석에 실패했습니다.";
    }

    public String requestAiAnalysisByPath(String filePath) {
        String pythonUrl = "http://localhost:8000/analyze";

        try {
            // 1. 가상 경로(/temp_uploads/...)를 실제 물리 경로로 변환
            String projectRoot = System.getProperty("user.dir");
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            String fullPath = projectRoot + File.separator + "storage" + File.separator + "uploads" + File.separator + fileName;

            File file = new File(fullPath);
            if (!file.exists()) {
                return "[시스템] 분석할 파일을 찾을 수 없습니다.";
            }

            // 2. 파이썬 서버로 보낼 요청 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // 저장된 파일을 다시 읽어서 리소스로 만듭니다.
            body.add("file", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. 파이썬 서버 호출 및 응답 처리
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 성공적으로 분석 결과를 받은 경우
                return response.getBody().get("analysis").toString();
            } else {
                return "[시스템] AI 분석 서버 응답 오류";
            }

        } catch (Exception e) {
            System.err.println("❌ 파일 경로 기반 분석 중 오류: " + e.getMessage());
            return "[시스템] 파일 분석 중 기술적 오류가 발생했습니다.";
        }
        // catch 블록 밖에도 리턴이 없으면 에러가 날 수 있으므로 최종 리턴을 보장합니다.
    }

    @Transactional
    public boolean acceptConsultation(String roomId, String lawyerId) {
        // 1. 방 상태 업데이트 (성공하면 1, 실패하면 0 반환됨)
        int roomResult = chatMapper.updateRoomStatus(roomId, "ONGOING");

        // 2. 변호사 상태 업데이트
        String realLawyerId = chatMapper.getLawyerEmailId(lawyerId);
        chatMapper.updateLawyerStatus(realLawyerId, "상담중");

        // 방 업데이트가 성공(1)했다면 true 반환
        return roomResult > 0;
    }

    public List<Map<String, Object>> getLawyerChatList(String lawyerId) {
        System.out.println("🔍 [목록 조회] 변호사 ID: " + lawyerId + "의 상담 리스트를 요청합니다.");

        // Mapper를 통해 DB에서 데이터를 가져옵니다.
        List<Map<String, Object>> chatList = chatMapper.getLawyerChatList(lawyerId);

        // 데이터 가공: 메시지가 전혀 없는 방의 경우 기본 문구를 넣어줍니다.
        if (chatList != null && !chatList.isEmpty()) {
            for (Map<String, Object> chat : chatList) {
                if (chat.get("lastMessage") == null) {
                    chat.put("lastMessage", "새로운 상담 요청이 도착했습니다.");
                }
            }
            System.out.println("✅ [조회 완료] 총 " + chatList.size() + "건의 목록을 반환합니다.");
        } else {
            System.out.println("ℹ️ [조회 결과] 현재 진행 중인 상담이 없습니다.");
        }

        return chatList;
    }

    @Transactional
    public void closeChat(String roomId, String lawyerId) {
        // 1. CHAT_ROOM 상태를 'CLOSED'로 변경
        chatMapper.updateRoomStatus(roomId, "CLOSED");

        // 2. TB_LAWYER 상태를 'ACTIVE'로 복구
        String realLawyerId = chatMapper.getLawyerEmailId(lawyerId);
        chatMapper.updateLawyerStatus(realLawyerId, "ACTIVE");

        log.info("상담 종료: 방번호 {}, 변호사 {} 상태를 'ACTIVE'로 복구", roomId, realLawyerId);
    }


}