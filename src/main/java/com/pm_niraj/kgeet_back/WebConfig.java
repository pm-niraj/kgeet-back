package com.pm_niraj.kgeet_back;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from your React frontend (e.g., http://localhost:3000)
        registry.addMapping("/**")  // Allow CORS for all paths
                .allowedOrigins("http://localhost:3000")  // Replace with your React app's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow necessary HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow sending cookies or authorization headers
    }
}
