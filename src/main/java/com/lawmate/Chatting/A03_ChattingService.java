package com.lawmate.Chatting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class A03_ChattingService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askGemini(String userMessage) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite-preview:generateContent?key="
                        + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 🔥 Gemini 요청 바디 구조
        Map<String, Object> textPart = Map.of("text", userMessage);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(parts));

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        // 🔥 Gemini 응답 파싱
        List candidates = (List) response.getBody().get("candidates");
        Map first = (Map) candidates.get(0);
        Map content = (Map) first.get("content");
        List partList = (List) content.get("parts");
        Map textMap = (Map) partList.get(0);



        return textMap.get("text").toString();
    }

}