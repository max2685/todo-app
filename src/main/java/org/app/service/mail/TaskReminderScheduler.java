package org.app.service.mail;

import lombok.RequiredArgsConstructor;
import org.app.entities.TaskEntity;
import org.app.repository.TodoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskReminderScheduler {
    private final TodoRepository todoRepository;
    private final EmailService emailService;

    //    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<TaskEntity> dueTasks = todoRepository.findAllByDueDate(tomorrow);
        List<TaskEntity> incompleteTasks = dueTasks
                .stream()
                .filter(task -> !task.isCompleted())
                .toList();

        incompleteTasks.forEach(task -> {
            String email = task.getUser().getUsername();
            String subject = "Task reminder: " + task.getTitle();
            String body = "Hello,\n\nThis is a reminder that your task \"" + task.getTitle() +
                    "\" is due tomorrow (" + task.getDueDate() + ")";
            emailService.sendReminderEmail(email, subject, body);
        });
    }
}
