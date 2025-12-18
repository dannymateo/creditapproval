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
                .title("¡Bienvenido a la Plataforma!")
                .subtitle("Cuenta creada exitosamente")
                .content(String.format(
                        "<p>Hola <strong>%s</strong>,</p>" +
                                "<p>Tu cuenta ha sido habilitada. Tu contraseña temporal es: <strong>%s</strong></p>" +
                                "<p>Te recomendamos cambiarla al ingresar por primera vez.</p>",
                        user.getEmail(), rawPassword))
                .action(EmailTemplateDTO.ActionDto.builder().title("Iniciar Sesión").url(loginUrl).build())
                .build();

        emailService.send(user.getEmail(), "Bienvenido - Tus credenciales de acceso", emailDto);
    }

    @Override
    public void sendPasswordResetEmail(User user, String rawPassword) {
        String loginUrl = clientUrl + "/login";
        EmailTemplateDTO emailDto = EmailTemplateDTO.builder()
                .title("Restablecimiento de Contraseña")
                .subtitle("Acción realizada por el administrador")
                .content(String.format(
                        "<p>Hola,</p>" +
                                "<p>Se ha generado una nueva contraseña temporal para tu cuenta: <strong>%s</strong></p>",
                        rawPassword))
                .action(EmailTemplateDTO.ActionDto.builder().title("Ir al Login").url(loginUrl).build())
                .build();

        emailService.send(user.getEmail(), "Alerta de Seguridad - Contraseña Restablecida", emailDto);
    }
}