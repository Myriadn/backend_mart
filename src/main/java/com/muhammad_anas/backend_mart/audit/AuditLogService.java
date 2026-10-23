package com.muhammad_anas.backend_mart.audit;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.muhammad_anas.backend_mart.config.AppProperties;
import com.muhammad_anas.backend_mart.security.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;

@Slf4j 
@Service 
@RequiredArgsConstructor 
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;

    /**
     * Records an audit entry. Must be called inside an existing transaction
     * so the audit entry commits (or rolls back) atomically with the business change.
     */
    @Transactional (propagation = Propagation.MANDATORY)
    public void log(String entityName, Long entityId, AuditAction action,
                    Object oldValue, Object newValue) {

        if (!appProperties.getAudit().isEnabled()) {
            return;
        }

        AuditLog entry = AuditLog.builder()
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .oldValue(serialize(oldValue))
                .newValue(serialize(newValue))
                .performedBy(SecurityUtils.currentUsername())
                .performedAt(Instant.now())
                .ipAddress(currentIpAddress())
                .build();

        auditLogRepository.save(entry);
    }

    public void logCreate(String entityName, Long entityId, Object newValue) {
        log(entityName, entityId, AuditAction.CREATE, null, newValue);
    }

    public void logUpdate(String entityName, Long entityId, Object oldValue, Object newValue) {
        log(entityName, entityId, AuditAction.UPDATE, oldValue, newValue);
    }

    public void logDelete(String entityName, Long entityId, Object oldValue) {
        log(entityName, entityId, AuditAction.DELETE, oldValue, null);
    }

    private String serialize(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException e) {
            // Serialization failure should NEVER break the business transaction.
            // Log it and store a marker instead.
            log.warn("Failed to serialize audit value for {}", value.getClass().getSimpleName(), e);
            return "<serialization_failed>";
        }
    }

    private String currentIpAddress() {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servletAttrs)) {
            return null;
        }
        HttpServletRequest request = servletAttrs.getRequest();

        // Behind a proxy/load balancer, X-Forwarded-For holds the real client IP.
        // Format: "client, proxy1, proxy2" — the leftmost is the original client.
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
