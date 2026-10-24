package com.muhammad_anas.backend_mart.auth.dto;

public record LoginResponse(
    
    String accessToken,
    String tokenType,
    long expiresIn,
    String username,
    String role
) {
    
}
