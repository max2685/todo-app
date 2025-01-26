package org.app.service.mail;

public interface EmailService {
    void sendReminderEmail(String to, String subject, String body);
}
