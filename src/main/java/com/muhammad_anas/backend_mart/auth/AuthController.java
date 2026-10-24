package com.muhammad_anas.backend_mart.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muhammad_anas.backend_mart.auth.dto.LoginRequest;
import com.muhammad_anas.backend_mart.auth.dto.LoginResponse;
import com.muhammad_anas.backend_mart.common.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("${app.api.base-path}/auth")
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.ok("Login successful", authService.login(req));
    }
}
