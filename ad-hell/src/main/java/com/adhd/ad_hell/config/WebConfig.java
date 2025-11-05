package com.adhd.ad_hell.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${image.image-dir}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 예: /uploads/** → 실제 파일 경로로 매핑
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + uploadDir + "/");
  }
}