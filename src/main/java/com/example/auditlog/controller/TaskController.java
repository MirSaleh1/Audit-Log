package com.example.auditlog.controller;

import com.example.auditlog.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public String createTask(@RequestParam String name) {
        taskService.createTask(name);
        return "Task yaradıldı";
    }
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task silindi";
    }

    @PutMapping("/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam String name) {
        taskService.updateTask(id, name);
        return "Task yeniləndi";
    }
}