package org.app.repository.todo;

import jakarta.persistence.criteria.Predicate;
import org.app.dto.todos.FilterDto;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TodoSpecification {

    public static Specification<TaskEntity> getSpecification(UserEntity user, FilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), user.getId()));

            if (filterDto.getTitle() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + filterDto.getTitle().toLowerCase() + "%"
                        )
                );
            }

            if (filterDto.getCreatedDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdDate"), filterDto.getCreatedDate()));
            }

            if (filterDto.getDueDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("dueDate"), filterDto.getDueDate()));
            }

            if (filterDto.getCompleted() != null) {
                predicates.add(criteriaBuilder.equal(root.get("completed"), filterDto.getCompleted()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}