package com.lawmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 외부 저장소(C:/lawmate_uploads/)에 있는 파일을
 * 브라우저에서 URL(/uploads/...)로 접근할 수 있게 해주는 설정입니다.
 */
@Configuration
public class ChatFileResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        String uploadPath = "file:///" + projectRoot + "/storage/uploads/";

        registry.addResourceHandler("/temp_uploads/**")
                .addResourceLocations(uploadPath);
    }
}