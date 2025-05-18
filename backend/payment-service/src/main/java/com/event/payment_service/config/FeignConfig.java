package com.event.payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {
    
    @Bean
    RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            public void apply(RequestTemplate template){
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String authHeader = request.getHeader("Authorization"); 
                    if (authHeader != null && !authHeader.isEmpty()) {
                        template.header("Authorization", authHeader);
                    }
                }
            }
        };
    }
}
