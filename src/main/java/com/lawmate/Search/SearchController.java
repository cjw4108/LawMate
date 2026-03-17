package com.lawmate.Search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("vectorSearchController")  // ← 빈 이름을 다르게 지정해서 충돌 방지
public class SearchController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", defaultValue = "") String query,
                         @RequestParam(value = "source", defaultValue = "") String source,
                         Model model) {

        model.addAttribute("query", query);
        model.addAttribute("sourceFilter", source);

        if (!query.isBlank()) {
            try {
                String url = "http://localhost:8001/search?q={q}&k=10"
                        + (source.isBlank() ? "" : "&source=" + source);

                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class, query);
                List<Map<String, Object>> raw = (List<Map<String, Object>>) response.getBody().get("results");

                // Python에서 이미 title/preview 분리해서 주므로 그대로 사용
                model.addAttribute("searchResults", raw);
                model.addAttribute("resultCount", raw.size());

            } catch (Exception e) {
                model.addAttribute("searchError", "검색 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
        return "search";
    }
}