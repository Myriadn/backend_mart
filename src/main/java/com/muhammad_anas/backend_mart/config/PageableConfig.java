package com.muhammad_anas.backend_mart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import lombok.RequiredArgsConstructor;

@Configuration 
@RequiredArgsConstructor 
public class PageableConfig {
    
    private final AppProperties appProperties;

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return resolver -> {
            resolver.setMaxPageSize(appProperties.getPagination().getMaxPageSize());
            resolver.setFallbackPageable(PageRequest.of(0,
                    appProperties.getPagination().getDefaultPageSize()));
        };
    }
}
