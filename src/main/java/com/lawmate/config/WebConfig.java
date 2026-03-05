package com.lawmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 브라우저가 /uploads/** 주소로 사진을 달라고 하면
        registry.addResourceHandler("/uploads/**")
                // 실제 프로젝트 폴더 바로 옆에 있는 uploads 폴더에서 찾아라!
                .addResourceLocations("file:uploads/");
    }
}