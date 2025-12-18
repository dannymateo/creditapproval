package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.notification;

import com.cotrafa.creditapproval.loanrequest.domain.model.NotificationData;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.shared.infrastructure.notification.EmailService;
import com.cotrafa.creditapproval.shared.infrastructure.notification.dto.EmailTemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LoanNotificationAdapter implements NotificationPort {

    private final EmailService emailService;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void sendReceivedEmail(NotificationData data) {
        String statusUrl = clientUrl + "/my-requests";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("Solicitud Recibida")
                .subtitle("Estamos trabajando en tu crédito")
                .content(String.format(
                        "<p>Hola <strong>%s</strong>,</p>" +
                                "<p>Hemos recibido tu solicitud para un <strong>%s</strong> por valor de <strong>%s</strong>.</p>" +
                                "<p>Nuestro equipo iniciará el estudio de crédito y te informaremos por este medio.</p>",
                        data.getCustomerName(), data.getLoanTypeName(), formatCurrency(data.getAmount())))
                .action(EmailTemplateDTO.ActionDto.builder().title("Ver mi Solicitud").url(statusUrl).build())
                .build();

        emailService.send(data.getToEmail(), "Confirmación de Solicitud - Cotrafa", emailDto);
    }

    @Override
    public void sendApprovedEmail(NotificationData data) {
        String loanUrl = clientUrl + "/my-loans";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("¡Felicidades!")
                .subtitle("Tu crédito ha sido aprobado")
                .content(String.format(
                        "<p>Excelentes noticias <strong>%s</strong>,</p>" +
                                "<p>Tu solicitud de <strong>%s</strong> ha sido <strong>APROBADA</strong>.</p>" +
                                "<p>El desembolso se realizará en las próximas horas según las condiciones pactadas.</p>",
                        data.getCustomerName(), data.getLoanTypeName()))
                .action(EmailTemplateDTO.ActionDto.builder().title("Ver mis Créditos").url(loanUrl).build())
                .build();

        emailService.send(data.getToEmail(), "Crédito Aprobado - Cotrafa", emailDto);
    }

    @Override
    public void sendRejectedEmail(NotificationData data) {
        String contactUrl = clientUrl + "/contact";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("Estado de Solicitud")
                .subtitle("Información sobre tu crédito")
                .content(String.format(
                        "<p>Estimado(a) <strong>%s</strong>,</p>" +
                                "<p>Sentimos informarte que tu solicitud de <strong>%s</strong> no ha sido aprobada en esta ocasión.</p>" +
                                "<p>Te invitamos a contactar con un asesor para conocer más detalles o intentarlo nuevamente en 6 meses.</p>",
                        data.getCustomerName(), data.getLoanTypeName()))
                .action(EmailTemplateDTO.ActionDto.builder().title("Contactar Asesor").url(contactUrl).build())
                .build();

        emailService.send(data.getToEmail(), "Respuesta a tu Solicitud - Cotrafa", emailDto);
    }

    private String formatCurrency(Object amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return formatter.format(amount);
    }
}