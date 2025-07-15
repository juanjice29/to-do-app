package com.example.todo.api;

import com.example.todo.dto.*;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin(origins = "*")
public class TaskController {
    @Autowired
    private TaskService service;
    @PostMapping()
    public ResponseEntity<TaskDetailResponseDTO> create(@Validated @RequestBody TaskCreateRequestDTO task){
        return ResponseEntity.ok(service.create(task));
    }
    @PatchMapping("/{taskId}/order")
    public ResponseEntity<Void> updateTaskOrder(
            @PathVariable Long taskId,
            @Validated @RequestBody UpdateTaskOrderRequestDTO orderRequest) {

        service.updateTaskOrder(taskId, orderRequest.getOrderBefore(), orderRequest.getOrderAfter());

        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<TaskListResponseDTO>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false, defaultValue = "taskOrder") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir
    ) {
        List<TaskListResponseDTO> tasks = service.findAllTasks(completed, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponseDTO> getTaskById(@PathVariable Long taskId) {
        TaskDetailResponseDTO task = service.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId){
        service.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequestDTO updateRequest) {

        TaskDetailResponseDTO updatedTask = service.updateTask(taskId, updateRequest);
        return ResponseEntity.ok(updatedTask);
    }

}
