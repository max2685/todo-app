package org.app.service;

import org.app.model.RecordingModel;

public interface TodoService {
    RecordingModel saveRecording(RecordingModel recording);
    RecordingModel getRecordingById(Long id);
    RecordingModel editRecording(Long id, RecordingModel recording);
    void deleteRecording(Long id);
}
