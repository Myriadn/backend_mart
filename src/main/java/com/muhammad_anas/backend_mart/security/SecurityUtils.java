package com.muhammad_anas.backend_mart.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {}

    public static String currentUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(a -> !"anonymousUser".equals(a.getPrincipal()))
                .map(Authentication::getName)
                .orElse("system");
    }
}
