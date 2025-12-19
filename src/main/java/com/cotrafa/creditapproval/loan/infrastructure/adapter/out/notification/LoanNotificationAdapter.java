package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.notification;

import com.cotrafa.creditapproval.loan.domain.model.Notification;
import com.cotrafa.creditapproval.loan.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.shared.infrastructure.notification.EmailService;
import com.cotrafa.creditapproval.shared.infrastructure.notification.dto.EmailTemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("LoanNotificationAdapter")
@RequiredArgsConstructor
public class LoanNotificationAdapter implements NotificationPort {

    private final EmailService emailService;

    @Override
    public void send(Notification notification) {
        EmailTemplateDTO baseDto = EmailTemplateDTO.builder()
                .title("Crédito Desembolsado")
                .content("¡Felicidades! Tu préstamo ha sido procesado exitosamente.")
                .footer("Cotrafa - Cooperativa Financiera")
                .build();

        emailService.send(
                notification.getTo(),
                notification.getSubject(),
                baseDto,
                notification.getTemplate(), // "loan-approval-template"
                notification.getVariables()
        );
    }
}