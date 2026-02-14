package com.lawmate.controller;

// ★ 이 부분들을 싹 다 복사해서 붙여넣으세요!
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DocsRankController {

    @GetMapping("/api/ranking")
    public List<String> getRanking() {
        List<String> fakeData = new ArrayList<>();
        fakeData.add("소장");
        fakeData.add("이혼");
        fakeData.add("지급명령");
        fakeData.add("내용증명");
        fakeData.add("가압류");
        fakeData.add("개인회생");

        return fakeData;
    }
}