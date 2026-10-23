package com.muhammad_anas.backend_mart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Validated 
@ConfigurationProperties (prefix = "app")
public class AppProperties {

    @Valid 
    private Api api = new Api();
    
    @Valid
    private Pagination pagination = new Pagination();
    
    @Valid
    private Security security = new Security();
    
    @Valid
    private Audit audit = new Audit();

    @Getter 
    @Setter
    public static class Api {
        
        @NotBlank 
        private String basePath = "/api/v1";
    }

    @Getter 
    @Setter
    public static class Pagination {
        
        @Min(1) 
        private int defaultPageSize = 20;
        
        @Min(1) 
        private int maxPageSize = 100;
    }

    @Getter 
    @Setter
    public static class Security {
        @Valid 
        private Jwt jwt = new Jwt();

        @Getter 
        @Setter
        public static class Jwt {
            
            @NotBlank 
            @Size(min = 32)
            private String secret;
            
            @Min(60_000) 
            private long expirationMs = 86_400_000L;
        }
    }

    @Getter 
    @Setter
    public static class Audit {
        private boolean enabled = true;
    }
}
