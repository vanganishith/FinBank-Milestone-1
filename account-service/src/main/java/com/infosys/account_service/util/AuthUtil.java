package com.infosys.account_service.util;

import com.infosys.account_service.feign.AuthFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthUtil {

    @Autowired
    AuthFeignClient authFeignClient;

    public Map<String, Object> validateAndGetClaims(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Map<String, Object> result = authFeignClient.validate(token);

        if (!Boolean.TRUE.equals(result.get("valid"))) {
            throw new RuntimeException("Invalid or expired token");
        }
        return result;
    }

    public void requireRole(String authHeader, String requiredRole) {
        Map<String, Object> claims = validateAndGetClaims(authHeader);
        String role = (String) claims.get("role");
        if (!requiredRole.equals(role)) {
            throw new com.infosys.account_service.exception.AccessDeniedException(
                    "Access denied. Required role: " + requiredRole + ", but got: " + role);
        }
    }
}