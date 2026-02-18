package com.lawmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class DocsRankController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/api/ranking")
    public List<String> getRanking() {
        String destKey = "search:rank:weekly";
        List<String> last7DaysKeys = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            String date = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            last7DaysKeys.add("search:rank:" + date);
        }

        redisTemplate.opsForZSet().unionAndStore(last7DaysKeys.get(0), last7DaysKeys, destKey);

        Set<String> rankingSet = redisTemplate.opsForZSet().reverseRange(destKey, 0, 5);

        if (rankingSet == null || rankingSet.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(rankingSet);
    }

    @PostMapping("/api/search/log")
    public void saveLog(@RequestParam("query") String query) {

        String cleanKeyword = query.replaceAll(" ", "").toLowerCase();
        String targetKeyword = cleanKeyword;

        if (cleanKeyword.contains("이혼")) {
            targetKeyword = "이혼";
        } else if (cleanKeyword.contains("고소")) {
            targetKeyword = "고소";
        } else if (cleanKeyword.contains("민사")) {
            targetKeyword = "민사";
        } else if (cleanKeyword.contains("임대") || cleanKeyword.contains("월세") || cleanKeyword.contains("전세")) {
            targetKeyword = "부동산";
        } else if (cleanKeyword.contains("노동") || cleanKeyword.contains("해고")) {
            targetKeyword = "노동";
        }

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String todayKey = "search:rank:" + today;

        redisTemplate.opsForZSet().incrementScore(todayKey, targetKeyword, 1.0);
        redisTemplate.expire(todayKey, 7, TimeUnit.DAYS);
    }
}