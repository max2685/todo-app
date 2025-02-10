package org.app.repository.todo;

import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    List<TaskEntity> findAllByDueDate(LocalDate dueDate);

    @Query("SELECT t FROM TaskEntity t WHERE t.user = :user " +
            "AND (CAST(:createdDate AS date) IS NULL OR t.createdDate = :createdDate) " +
            "AND (CAST(:dueDate AS date) IS NULL OR t.dueDate = :dueDate) " +
            "AND (:completed IS NULL OR t.completed = :completed) " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER('%' || CAST(:title AS text) || '%'))")
    List<TaskEntity> filterTasks(@Param("user") UserEntity user,
                                 @Param("createdDate") LocalDate createdDate,
                                 @Param("dueDate") LocalDate dueDate,
                                 @Param("completed") Boolean completed,
                                 @Param("title") String title);

}