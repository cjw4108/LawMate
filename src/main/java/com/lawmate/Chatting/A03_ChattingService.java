package com.lawmate.Chatting;

import ch.qos.logback.core.util.Loader;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument; // DOCX용
import java.nio.file.Files;
import java.util.Base64;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
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
        // 1. 여기서 lawyerId는 컨트롤러에서 보낸 "GEMINI_AI"여야 합니다.
        log.info("로그: findRoom 호출 직전 - userId={}, lawyerId={}", userId, lawyerId);

        String roomId = chatMapper.findRoom(userId, lawyerId);

        if (roomId != null && !roomId.isEmpty()) {
            log.info("로그: 방 찾기 성공! roomId={}", roomId);
            return roomId;
        }

        log.warn("로그: 방 찾기 실패. 새로 생성합니다.");

        // 2. 여기서만 변환을 수행해야 합니다.
        String realLawyerId = "GEMINI_AI".equals(lawyerId) ? "GEMINI_AI" : chatMapper.getLawyerEmailId(lawyerId);
        if (realLawyerId == null) realLawyerId = lawyerId;

        String newRoomId = UUID.randomUUID().toString();
        chatMapper.createChatRoom(newRoomId, userId, realLawyerId);

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
    public String askGemini(String userMessage, String fileData) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + API_KEY;

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 질문 내용 결정
        String finalPrompt = (userMessage == null || userMessage.trim().isEmpty() || userMessage.equals("(첨부파일 전송)"))
                ? "이 이미지(또는 문서)의 내용을 자세히 분석해서 설명해줘."
                : userMessage;

        // 3. API 몸체(Body) 구조 조립
        Map<String, Object> body = new java.util.HashMap<>();
        List<Map<String, Object>> parts = new java.util.ArrayList<>();

        // 🌟 [중요] 이미지 데이터 처리
        if (fileData != null && fileData.startsWith("[IMAGE_DATA]:")) {
            String base64Data = fileData.replace("[IMAGE_DATA]:", "");

            // 텍스트 파트 먼저 추가
            parts.add(Map.of("text", finalPrompt));

            // 이미지 파트 추가 (inline_data)
            Map<String, Object> inlineData = new java.util.HashMap<>();
            inlineData.put("mime_type", "image/png"); // PNG, JPG 모두 png로 보내도 대개 인식합니다.
            inlineData.put("data", base64Data);
            parts.add(Map.of("inline_data", inlineData));

            System.out.println("📸 [Gemini 전송] 이미지 데이터 포함됨 (길이: " + base64Data.length() + ")");
        }
        // 🌟 [중요] 일반 문서(PDF/DOCX) 처리
        else if (fileData != null && !fileData.isEmpty()) {
            String combinedText = finalPrompt + "\n\n[첨부 문서 내용]:\n" + fileData;
            parts.add(Map.of("text", combinedText));
            System.out.println("📄 [Gemini 전송] 문서 텍스트 포함됨 (길이: " + fileData.length() + ")");
        }
        // 일반 채팅
        else {
            parts.add(Map.of("text", finalPrompt));
        }

        body.put("contents", List.of(Map.of("parts", parts)));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            // 응답 추출 (안전한 체이닝)
            List candidates = (List) response.getBody().get("candidates");
            Map first = (Map) candidates.get(0);
            Map content = (Map) first.get("content");
            List resParts = (List) content.get("parts");

            return ((Map) resParts.get(0)).get("text").toString();

        } catch (Exception e) {
            System.err.println("❌ Gemini 호출 에러: " + e.getMessage());
            return "AI 분석 중 오류가 발생했습니다: " + e.getMessage();
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

    public String requestAiAnalysisByPath(String filePath) {
        String pythonUrl = "http://localhost:8000/analyze";
        try {
            String absolutePath = getAbsolutePath(filePath);
            File file = new File(absolutePath);

            if (!file.exists()) return "[시스템] 분석할 파일을 찾을 수 없습니다.";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 🚀 파이썬 서버 호출 전 로그
            System.err.println("🚀 [파이썬 전송 직전] URL: " + pythonUrl + " | 파일명: " + file.getName());

            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 파이썬 main.py에서 return {"analysis": "..."} 라고 보낸 값을 가져옴
                Object analysisResult = response.getBody().get("analysis");
                System.err.println("✅ [파이썬 응답 성공] 결과 수신 완료");
                return (analysisResult != null) ? analysisResult.toString() : "분석 결과가 비어있습니다.";
            } else {
                return "[시스템] AI 서버 응답 오류: " + response.getStatusCode();
            }
        } catch (Exception e) {
            System.err.println("❌ [통신 에러] " + e.getMessage());
            return "[시스템] 분석 중 서버 통신 오류가 발생했습니다.";
        }



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

    public String findRoom(String userId, String lawyerId) {
        // 이미 주입된 chatMapper를 사용하여 DB를 조회합니다.
        return chatMapper.findRoom(userId, lawyerId);
    }


    /**
     * PDF 파일로부터 텍스트를 추출하는 메서드
     * @param filePath DB에 저장된 상대 경로 (예: /temp_uploads/uuid_파일명.pdf)
     * @return 추출된 텍스트 문자열
     */
    public String extractTextFromPdf(String filePath) {
        // 1. 물리 경로 확보 (getAbsolutePath 메서드 활용)
        String absolutePath = getAbsolutePath(filePath);

        System.out.println(">>> [PDF 분석 시도] 실제 경로: " + absolutePath);

        File file = new File(absolutePath);
        if (!file.exists()) {
            System.err.println("!!! [파일 없음] 경로 확인 필요: " + absolutePath);
            return "[에러: 파일을 찾을 수 없습니다]";
        }

        // 2. PDFBox 3.0.x 버전 권장 방식으로 로드
        // Loader.loadPDF(File) 형식을 사용하는 것이 가장 안정적입니다.
        try (PDDocument document = org.apache.pdfbox.Loader.loadPDF(file)) {

            System.out.println(">>> [성공] PDF 문서 로드 완료. 텍스트 추출 시작...");

            PDFTextStripper stripper = new PDFTextStripper();

            // PDF 내의 텍스트를 순서대로 가져오도록 설정
            stripper.setSortByPosition(true);

            String text = stripper.getText(document);

            if (text == null || text.trim().isEmpty()) {
                System.out.println(">>> [결과] 텍스트가 비어있음 (이미지 PDF 가능성)");
                return "[안내: 이 PDF는 이미지로 구성되어 있어 텍스트 추출이 불가능합니다.]";
            }

            // 공백 및 줄바꿈 정리 (AI가 읽기 좋게)
            text = text.replaceAll("\\s+", " ").trim();

            System.out.println(">>> [완료] 추출 성공! 추출된 글자 수: " + text.length());
            return text;

        } catch (Exception e) {
            System.err.println("!!! [PDF 분석 중 에러 발생]");
            e.printStackTrace();
            return "[에러: PDF 분석 중 오류 발생 (" + e.getMessage() + ")]";
        }
    }

    public String analyzeFileContent(String filePath) {
        String extension = "";
        if (filePath.contains(".")) {
            extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        }

        System.out.println("📂 [분석 시작] 확장자: " + extension + " | 경로: " + filePath);

        switch (extension) {
            case "pdf": return extractTextFromPdf(filePath);
            case "docx": return extractTextFromDocx(filePath);
            case "jpg":
            case "jpeg":
            case "png":
                String base64 = encodeImageToBase64(filePath);
                if (base64 == null || base64.isEmpty()) {
                    System.err.println("❌ [분석 실패] 이미지 인코딩 결과가 비어있음!");
                    return null;
                }
                System.out.println("✅ [분석 성공] 이미지 Base64 변환 완료 (길이: " + base64.length() + ")");
                return "[IMAGE_DATA]:" + base64;
            default:
                System.err.println("⚠️ [미지원 확장자] " + extension);
                return null;
        }
    }

    private String encodeImageToBase64(String filePath) {
        try {
            String absolutePath = getAbsolutePath(filePath); // 여기서 실제 물리 경로를 조립
            File file = new File(absolutePath);

            if (!file.exists()) {
                System.err.println("❌ [파일 읽기 실패] 실제 경로에 파일이 없습니다: " + absolutePath);
                return "";
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            System.err.println("❌ [인코딩 에러] " + e.getMessage());
            return "";
        }
    }

    private String getAbsolutePath(String filePath) {
        // 파일명만 추출 (경로에 포함된 UUID_파일명 전체)
        String fileName = filePath;
        if (filePath.contains("/")) {
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        }

        // 🌟 절대 경로 조립 (사용자님의 실제 환경)
        // URL 인코딩된 문자(%20 등)가 섞여있을 수 있으므로 파일 객체 생성 시 주의
        String absolutePath = "C:\\Users\\human-11\\IdeaProjects\\project\\LawMate\\storage\\uploads\\" + fileName;

        // [중요 디버깅 로그] - 이 로그가 '성공'인지 '실패'인지 반드시 확인하세요!
        File file = new File(absolutePath);
        if (file.exists()) {
            System.err.println("✅ [파일 확인 성공] 실제 경로에 파일이 존재합니다: " + absolutePath);
        } else {
            System.err.println("❌ [파일 확인 실패] 경로가 틀렸습니다: " + absolutePath);
        }

        return absolutePath;
    }

    private String extractTextFromDocx(String filePath) {
        String absolutePath = getAbsolutePath(filePath); // 물리 경로 확보
        File file = new File(absolutePath);

        if (!file.exists()) {
            System.err.println("❌ [DOCX 읽기 실패] 파일이 없습니다: " + absolutePath);
            return "[에러: 파일을 찾을 수 없습니다]";
        }

        try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            StringBuilder sb = new StringBuilder();
            document.getParagraphs().forEach(p -> sb.append(p.getText()).append("\n"));

            System.out.println("✅ [DOCX 추출 성공] 글자 수: " + sb.length());
            return sb.toString();
        } catch (Exception e) {
            System.err.println("❌ [DOCX 분석 에러] " + e.getMessage());
            return "[에러: 워드 파일 분석 중 오류 발생]";
        }

    }
    public boolean isAiRoom(String roomId) {
        return chatMapper.countAiRoom(roomId) > 0;
    }

}