package com.example.todo.service;

import com.example.todo.dto.TaskCreateRequestDTO;
import com.example.todo.dto.TaskDetailResponseDTO;
import com.example.todo.dto.TaskListResponseDTO;
import com.example.todo.dto.TaskUpdateRequestDTO;
import com.example.todo.entity.Task;
import com.example.todo.error.ApiExceptionBussines;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita las anotaciones de Mockito
class TaskServiceTest {

    @Mock // Crea un mock (simulación) del repositorio
    private TaskRepository taskRepository;

    @InjectMocks // Crea una instancia de TaskService e inyecta los mocks (taskRepository) en ella
    private TaskService taskService;

    private Task task;
    private TaskCreateRequestDTO createRequestDTO;
    private TaskUpdateRequestDTO updateRequestDTO;

    @BeforeEach
        // Este método se ejecuta antes de cada prueba
    void setUp() {
        // Preparamos datos comunes para las pruebas
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setTaskOrder(10.0);
        task.setCreatedAt(LocalDateTime.now());

        createRequestDTO = new TaskCreateRequestDTO();
        createRequestDTO.setTitle("New Task");
        createRequestDTO.setDescription("New Description");
        createRequestDTO.setCompleted(false);

        updateRequestDTO = new TaskUpdateRequestDTO();
        updateRequestDTO.setTitle("Updated Title");
    }

    //--- Pruebas para el método create() ---//

    @Test
    @DisplayName("Debe crear la primera tarea con orden 1.0")
    void shouldCreateFirstTask() {
        // 1. Arrange (Preparación)
        // Simulamos que no hay tareas en la BD
        when(taskRepository.findTopByOrderByTaskOrderAsc()).thenReturn(Optional.empty());
        // Simulamos la acción de guardar
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act (Actuación)
        TaskDetailResponseDTO result = taskService.create(createRequestDTO);

        // 3. Assert (Verificación)
        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals(1.0, result.getOrder()); // La primera tarea debe tener orden 1.0
        verify(taskRepository).save(any(Task.class)); // Verificamos que se llamó al método save
    }

    @Test
    @DisplayName("Debe crear una nueva tarea con un orden menor al existente")
    void shouldCreateNewTaskWhenTasksExist() {
        // Arrange
        // Simulamos que ya existe una tarea con orden 10.0
        when(taskRepository.findTopByOrderByTaskOrderAsc()).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskDetailResponseDTO result = taskService.create(createRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(9.0, result.getOrder()); // El orden debe ser 10.0 - 1.0 = 9.0
        verify(taskRepository).save(any(Task.class));
    }

    //--- Pruebas para el método findTaskById() ---//

    @Test
    @DisplayName("Debe encontrar una tarea por su ID")
    void shouldFindTaskById() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDetailResponseDTO result = taskService.findTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    @DisplayName("Debe lanzar una excepción si la tarea no se encuentra")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Arrange
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApiExceptionBussines.class, () -> {
            taskService.findTaskById(99L);
        });
    }

    //--- Pruebas para el método findAllTasks() ---//

    @Test
    @DisplayName("Debe devolver una lista de tareas")
    void shouldFindAllTasks() {
        // Arrange
        when(taskRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Collections.singletonList(task));

        // Act
        List<TaskListResponseDTO> results = taskService.findAllTasks(null, "taskOrder", "asc");

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Test Task", results.get(0).getTitle());
    }

    //--- Pruebas para el método updateTask() ---//

    @Test
    @DisplayName("Debe actualizar una tarea existente")
    void shouldUpdateTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskDetailResponseDTO result = taskService.updateTask(1L, updateRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle()); // El título debe estar actualizado
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar una tarea que no existe")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Arrange
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApiExceptionBussines.class, () -> {
            taskService.updateTask(99L, updateRequestDTO);
        });
        verify(taskRepository, never()).save(any(Task.class)); // save no debe ser llamado
    }

    //--- Pruebas para el método deleteTask() ---//

    @Test
    @DisplayName("🗑Debe eliminar una tarea")
    void shouldDeleteTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(1L); // No hace nada cuando se llama a deleteById

        // Act
        assertDoesNotThrow(() -> taskService.deleteTask(1L));

        // Assert
        verify(taskRepository).deleteById(1L); // Verifica que deleteById fue llamado
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar una tarea que no existe")
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Arrange
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApiExceptionBussines.class, () -> {
            taskService.deleteTask(99L);
        });
        verify(taskRepository, never()).deleteById(anyLong()); // deleteById nunca debe ser llamado
    }
}
