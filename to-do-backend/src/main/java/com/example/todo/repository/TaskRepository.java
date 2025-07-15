package com.example.todo.repository;

import com.example.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findTopByOrderByTaskOrderAsc();
}
