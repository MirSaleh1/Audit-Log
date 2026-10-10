package com.example.auditlog.config;

import com.example.auditlog.entity.AuditLog;
import com.example.auditlog.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;


    @AfterReturning(pointcut = "@annotation(auditable)")
    public void logActivity(JoinPoint joinPoint, Auditable auditable) {

        AuditLog auditLog = new AuditLog();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLog.setActorId(username);
        auditLog.setActionType(auditable.action());
        auditLog.setRiskLevel(auditable.riskLevel());
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setCreatedAt(LocalDateTime.now());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            auditLog.setTargetResource("Task ID: " + args[0].toString());
        }
        auditLogRepository.save(auditLog);
    }
}