package com.example.auditlog.service;

import com.example.auditlog.config.Auditable;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TaskService {

    @Auditable(action = "CREATE_TASK", riskLevel = "LOW")
    public void createTask(String taskName) {
        log.info("Task yaradıldı: " + taskName);
    }

    @Auditable(action = "DELETE_TASK", riskLevel = "MEDIUM")
    public void deleteTask(Long taskId) {
       log.info("Task silindi: ID = " + taskId);
    }

    @Auditable(action = "UPDATE_TASK", riskLevel = "LOW")
    public void updateTask(Long taskId, String newName) {
       log.info("Task yeniləndi: ID = " + taskId + " Name = " + newName);
    }
}
