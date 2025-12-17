package com.cotrafa.creditapproval.user.infrastructure.adapter.out.notification;

import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.shared.infrastructure.notification.EmailService;
import com.cotrafa.creditapproval.shared.infrastructure.notification.dto.EmailTemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationAdapter implements NotificationPort {

    private final EmailService emailService;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void sendWelcomeEmail(User user, String rawPassword) {
        String loginUrl = clientUrl + "/login";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("Welcome to the Platform")
                .subtitle("Account Created Successfully")
                .content(String.format("<p>Hello <strong>%s</strong>, your password is: <strong>%s</strong></p>", user.getEmail(), rawPassword))
                .action(EmailTemplateDTO.ActionDto.builder().title("Login Now").url(loginUrl).build())
                .build();

        emailService.send(user.getEmail(), "Welcome - Your Credentials", emailDto);
    }

    @Override
    public void sendPasswordResetEmail(User user, String rawPassword) {
        String loginUrl = clientUrl + "/login";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("Password Reset by Admin")
                .content(String.format("<p>New temporary password: <strong>%s</strong></p>", rawPassword))
                .action(EmailTemplateDTO.ActionDto.builder().title("Login Now").url(loginUrl).build())
                .build();

        emailService.send(user.getEmail(), "Security Alert - Password Reset", emailDto);
    }
}