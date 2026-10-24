package com.muhammad_anas.backend_mart.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muhammad_anas.backend_mart.auth.dto.LoginRequest;
import com.muhammad_anas.backend_mart.auth.dto.LoginResponse;
import com.muhammad_anas.backend_mart.config.AppProperties;
import com.muhammad_anas.backend_mart.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppProperties appProperties;

    public LoginResponse login(LoginRequest req) {
        // Throws BadCredentialsException
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .findFirst()
                .orElse("UNKNOWN");

        long expiresInSeconds = appProperties.getSecurity().getJwt().getExpirationMs() / 1000;

        return new LoginResponse(token, "Bearer", expiresInSeconds,
                userDetails.getUsername(), role);
    }
}
