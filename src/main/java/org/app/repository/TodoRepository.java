package org.app.repository;

import org.app.model.RecordingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<RecordingModel, Long> {
}
