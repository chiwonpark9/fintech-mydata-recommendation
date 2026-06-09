package com.team7.security.config;

import com.amazonaws.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class corsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
                .addMapping("/**")
                .allowedHeaders("Authorization", "Refresh", "*")
                .exposedHeaders("Authorization", "Refresh", "*")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedOrigins( "http://localhost:5173","http://localhost:3000");
    }

}
