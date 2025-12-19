package com.cotrafa.creditapproval.shared.infrastructure.notification;

import com.cotrafa.creditapproval.shared.infrastructure.notification.dto.EmailTemplateDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void send(String to, String subject, EmailTemplateDTO templateDto,
                     String templateName, Map<String, Object> extraVariables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();

            context.setVariable("title", templateDto.getTitle());
            context.setVariable("subtitle", templateDto.getSubtitle());
            context.setVariable("banner", templateDto.getBanner());
            context.setVariable("content", templateDto.getContent());
            context.setVariable("description", templateDto.getDescription());
            context.setVariable("action", templateDto.getAction());
            context.setVariable("footer", templateDto.getFooter());

            if (extraVariables != null) {
                for (Map.Entry<String, Object> entry : extraVariables.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            }
            String htmlBody = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom(senderEmail);

            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }
    @Async
    public void send(String to, String subject, EmailTemplateDTO templateDto) {
        this.send(to, subject, templateDto, "email", null);
    }
}