package com.example.auditlog.config;

import com.example.auditlog.entity.AuditLog;
import com.example.auditlog.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthenticationAuditListener {

    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        AuditLog auditLog = new AuditLog();
        auditLog.setActorId(username);
        auditLog.setActionType("LOGIN_SUCCESS");
        auditLog.setRiskLevel("LOW");
        auditLog.setIpAddress(getIpAddress());
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    @EventListener
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        AuditLog auditLog = new AuditLog();
        auditLog.setActorId(username);
        auditLog.setActionType("LOGIN_FAILED");
        auditLog.setRiskLevel("HIGH");
        auditLog.setIpAddress(getIpAddress());
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setTargetResource("Reason: " + event.getException().getMessage());
        auditLogRepository.save(auditLog);
    }

    private String getIpAddress() {
        try {
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}
