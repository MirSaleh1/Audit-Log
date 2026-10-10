package com.example.auditlog.service;

import com.example.auditlog.entity.AuditLog;
import com.example.auditlog.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> searchAuditLogs(String actorId, String actionType, String targetResource, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findAll((Specification<AuditLog>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (actorId != null && !actorId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("actorId"), actorId));
            }
            if (actionType != null && !actionType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("actionType"), actionType));
            }
            if (targetResource != null && !targetResource.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("targetResource"), "%" + targetResource + "%"));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public void exportToCsv(PrintWriter writer, List<AuditLog> logs) {
        writer.println("ID,Actor ID,Action Type,Target Resource,IP Address,Created At,Risk Level");
        for (AuditLog log : logs) {
            writer.printf("%d,%s,%s,%s,%s,%s,%s%n",
                    log.getId(),
                    log.getActorId(),
                    log.getActionType(),
                    log.getTargetResource() != null ? log.getTargetResource().replace(",", ";") : "",
                    log.getIpAddress(),
                    log.getCreatedAt(),
                    log.getRiskLevel()
            );
        }
    }
}
