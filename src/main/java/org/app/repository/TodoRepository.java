package org.app.repository;

import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByUser(UserEntity user);

    List<TaskEntity> findAllByDueDate(LocalDate dueDate);

    @Query("SELECT t FROM TaskEntity t WHERE t.user = :user " +
            "AND (CAST(:createdDate as DATE) IS NULL OR t.createdDate = CAST(:createdDate as DATE)) " +
            "AND (CAST(:dueDate as DATE) is NULL OR t.dueDate = CAST(:dueDate as DATE)) " +
            "AND (:completed = true OR t.completed = false) " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<TaskEntity> filterTasks(@Param("user") UserEntity user,
                                 @Param("createdDate") LocalDate createdDate,
                                 @Param("dueDate") LocalDate dueDate,
                                 @Param("completed") boolean completed,
                                 @Param("title") String title);
}
