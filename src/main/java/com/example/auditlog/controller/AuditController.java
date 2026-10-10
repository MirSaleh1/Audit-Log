package com.example.auditlog.controller;

import com.example.auditlog.entity.AuditLog;
import com.example.auditlog.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/audit")
public class AuditController {

    private final AuditLogService auditLogService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuditLog> getAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public List<AuditLog> searchAuditLogs(
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) String targetResource,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return auditLogService.searchAuditLogs(actorId, actionType, targetResource, startDate, endDate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void exportAuditLogsToCsv(
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) String targetResource,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit_logs.csv\"");
        
        List<AuditLog> logs = auditLogService.searchAuditLogs(actorId, actionType, targetResource, startDate, endDate);
        auditLogService.exportToCsv(response.getWriter(), logs);
    }
}
