package com.lawmate.controller;

public class DocsRankController {

    @RestController
    public class RankApiController {

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
}
