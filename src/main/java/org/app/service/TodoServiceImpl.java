package org.app.service;

import org.app.model.RecordingModel;
import org.app.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public RecordingModel saveRecording(RecordingModel recording) {
        recording.setCreatedDate(LocalDate.now());
        return todoRepository.save(recording);
    }

    @Override
    public RecordingModel getRecordingById(Long id) {
        return todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public RecordingModel editRecording(Long id, RecordingModel recordingDetails) {
        RecordingModel recordingModel = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        recordingModel.setTitle(recordingDetails.getTitle());
        recordingModel.setComment(recordingDetails.getComment());
        recordingModel.setCreatedDate(recordingDetails.getCreatedDate());
        recordingModel.setDueDate(recordingDetails.getDueDate());
        recordingModel.setCompleted(recordingDetails.isCompleted());
        return todoRepository.save(recordingModel);
    }

    @Override
    public void deleteRecording(Long id) {
        todoRepository.deleteById(id);
    }
}
