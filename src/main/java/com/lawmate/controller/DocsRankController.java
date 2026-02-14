package com.lawmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
public class DocsRankController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/api/ranking")
    public List<String> getRanking() {

        Set<String> rankingSet = redisTemplate.opsForZSet().reverseRange("search:rank", 0, 5);
        if (rankingSet == null || rankingSet.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(rankingSet);
    }
    @PostMapping("/api/search/log")
    public void saveLog(@RequestParam("query") String query) {

        String cleanKeyword = query.replaceAll(" ", "");

        if (cleanKeyword.contains("이혼")) {
            cleanKeyword = "이혼";
        }
        else if (cleanKeyword.contains("고소")) {
            cleanKeyword = "고소";
        }
        else if (cleanKeyword.contains("임대")) {
            cleanKeyword = "임대차";
        }

        redisTemplate.opsForZSet().incrementScore("search:rank", cleanKeyword, 1.0);
    }
}