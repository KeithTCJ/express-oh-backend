package com.team_one.expressoh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(false);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // Map the URL Path to the external registry
        registry.addResourceHandler("/" + uploadDir + "/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}