package org.app.service.mail;

import lombok.RequiredArgsConstructor;
import org.app.entities.TaskEntity;
import org.app.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class TaskReminderScheduler implements SchedulingConfigurer {
    private final TodoRepository todoRepository;
    private final EmailService emailService;

    @Value("${email.schedule}")
    private String emailSchedule;

    @Transactional
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<TaskEntity> dueTasks = todoRepository.findAllByDueDate(tomorrow);
        List<TaskEntity> incompleteTasks = dueTasks
                .stream()
                .filter(task -> !task.getCompleted())
                .toList();

        incompleteTasks.forEach(task -> {
            String email = task.getUser().getUsername();
            String subject = "Task reminder: " + task.getTitle();
            String body = "Hello,\n\nThis is a reminder that your task \"" + task.getTitle() +
                    "\" is due tomorrow (" + task.getDueDate() + ")";
            emailService.sendReminderEmail(email, subject, body);
        });
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newSingleThreadScheduledExecutor());

        taskRegistrar.addCronTask(this::sendReminders, emailSchedule);
    }
}
