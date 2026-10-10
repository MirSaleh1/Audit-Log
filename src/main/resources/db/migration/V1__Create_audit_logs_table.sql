CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_id VARCHAR(255) NOT NULL,
    action_type VARCHAR(255) NOT NULL,
    target_resource VARCHAR(255),
    ip_address VARCHAR(45),
    created_at TIMESTAMP NOT NULL,
    risk_level VARCHAR(50)
);
