package com.example.todo.service;

import com.example.todo.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class TaskSpecification {

    public static Specification<Task> build(Boolean completed) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (completed != null) {
                predicates.add(criteriaBuilder.equal(root.get("completed"), completed));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
