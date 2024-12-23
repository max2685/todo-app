package org.app.controller;

import org.app.model.RecordingModel;
import org.app.service.TodoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TodoController {
    @Autowired
    private TodoServiceImpl todoServiceImpl;

    @PostMapping
    public ResponseEntity<RecordingModel> saveRecording(@RequestBody RecordingModel recording) {
        RecordingModel createdRecording = todoServiceImpl.saveRecording(recording);
        return ResponseEntity.ok(createdRecording);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordingModel> getRecording(@PathVariable("id") Long id) {
        RecordingModel recording = todoServiceImpl.getRecordingById(id);
        return ResponseEntity.ok(recording);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordingModel> updateRecording(@PathVariable("id") Long id, @RequestBody RecordingModel recordingModel) {
        RecordingModel updatedRecording = todoServiceImpl.editRecording(id, recordingModel);
        return ResponseEntity.ok(updatedRecording);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecording(@PathVariable("id") Long id) {
        todoServiceImpl.deleteRecording(id);
        return ResponseEntity.noContent().build();
    }
}
