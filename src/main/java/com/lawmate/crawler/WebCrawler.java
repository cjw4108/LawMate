package com.lawmate.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WebCrawler {
    public static void main(String[] args) {
        // 크롤링할 웹 페이지 URL
        String url = "https://www.law.go.kr/main.html";

        try {
            // URL로부터 HTML 문서 가져오기
            Document doc = Jsoup.connect(url).get();

            // HTML 문서에서 원하는 데이터 추출 (예: 제목 가져오기)
            String title = doc.title();
            System.out.println("Page Title: " + title);

            // 예시: 모든 링크 가져오기
            for (Element link : doc.select("a[href]")) {
                System.out.println("Link: " + link.attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}