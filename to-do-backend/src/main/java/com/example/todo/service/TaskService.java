package com.example.todo.service;

import com.example.todo.dto.TaskCreateRequestDTO;
import com.example.todo.dto.TaskDetailResponseDTO;
import com.example.todo.dto.TaskListResponseDTO;
import com.example.todo.dto.TaskUpdateRequestDTO;
import com.example.todo.entity.Task;
import com.example.todo.error.ApiExceptionBussines;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public TaskDetailResponseDTO create(TaskCreateRequestDTO task){
        Optional<Task> firstTask = repository.findTopByOrderByTaskOrderAsc();

        Task newTask= new Task();
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setCompleted(task.isCompleted());
        if (firstTask.isPresent()) {
            newTask.setTaskOrder(firstTask.get().getTaskOrder() - 1.0);
        } else {
            newTask.setTaskOrder(1.0);
        }

        repository.save(newTask);
        return TaskDetailResponseDTO.mapToDetailDTO(newTask);
    }
    public Task updateTaskOrder(Long taskId, Double orderBefore, Double orderAfter) {

        Task taskToMove = repository.findById(taskId)
                .orElseThrow(() -> new ApiExceptionBussines("Task not found", HttpStatus.BAD_REQUEST));

        double newOrder;

        if (orderBefore != null && orderAfter != null) {
            newOrder = (orderBefore + orderAfter) / 2.0;
        } else if (orderBefore == null && orderAfter != null) {
            newOrder = orderAfter - 1.0;
        } else if (orderBefore != null && orderAfter == null) {
            newOrder = orderBefore + 1.0;
        } else {
            newOrder = 1.0;
        }

        taskToMove.setTaskOrder(newOrder);
        return repository.save(taskToMove);
    }

    public List<TaskListResponseDTO> findAllTasks(Boolean completed, String sortBy, String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);

        // 2. Construir la especificación para el filtrado
        Specification<Task> spec = TaskSpecification.build(completed);

        // 3. Ejecutar la consulta
        List<Task> tasks = repository.findAll(spec, sort);

        // 4. Mapear la lista de Entidades a una lista de DTOs
        return tasks.stream()
                .map(task -> TaskListResponseDTO.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .completed(task.isCompleted())
                        .order(task.getTaskOrder())
                        .createdAt(task.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    public TaskDetailResponseDTO findTaskById(Long taskId) {
        // Busca la tarea por ID, si no la encuentra, lanza una excepción.
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new ApiExceptionBussines("Task not found with id: " + taskId, HttpStatus.NOT_FOUND));

        // Mapea la entidad encontrada a un DTO de respuesta detallado.
        return TaskDetailResponseDTO.mapToDetailDTO(task);
    }

    public void deleteTask(Long id){
        repository.findById(id)
                .orElseThrow(() -> new ApiExceptionBussines("Task not found", HttpStatus.BAD_REQUEST));
        repository.deleteById(id);
    }

    public TaskDetailResponseDTO updateTask(Long taskId, TaskUpdateRequestDTO updateRequest) {

        Task taskToUpdate = repository.findById(taskId)
                .orElseThrow(() -> new ApiExceptionBussines("Task not found", HttpStatus.NOT_FOUND));


        if (updateRequest.getTitle() != null) {
            taskToUpdate.setTitle(updateRequest.getTitle());
        }

        if (updateRequest.getDescription() != null) {
            taskToUpdate.setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getCompleted() != null) {
            taskToUpdate.setCompleted(updateRequest.getCompleted());
        }


        Task savedTask = repository.save(taskToUpdate);

        // Mapea la entidad guardada a un DTO de respuesta (puedes crear un TaskDetailResponseDTO)
        return TaskDetailResponseDTO.mapToDetailDTO(savedTask);
    }

}
