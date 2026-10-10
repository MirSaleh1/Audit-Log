package com.example.auditlog.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "actor_id", nullable = false)
    private String actorId;
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "target_resource")
    private String targetResource;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "risk_level")
    private String riskLevel;
}
