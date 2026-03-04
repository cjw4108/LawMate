package com.lawmate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**") // 관리자 경로는 감시하되
                .excludePathPatterns(
                        "/admin/login",          // 로그인 페이지 허용
                        "/css/**",               // CSS 파일 허용
                        "/js/**",                // 자바스크립트 허용
                        "/images/**",            // 이미지 파일 허용
                        "/assets/**",            // 기타 자산 허용
                        "/favicon.ico"           // 아이콘 허용
                );
    }
}