package org.app.factories;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionFactory {

    public static ResponseStatusException userNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    public static ResponseStatusException taskNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }

    public static ResponseStatusException unauthorizedTaskAction() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission to do that");
    }

    public static ResponseStatusException dueDateNotCorrect() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Due date cannot be in the past");
    }
}