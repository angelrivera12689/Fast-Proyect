package com.app.ventas_api.seguridad.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio de Email para enviar códigos de verificación y notificaciones
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Enviar código de verificación para recuperación de contraseña
     */
    public void sendPasswordResetCode(String toEmail, String username, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🔐 Código de recuperación de contraseña - FAST");
        message.setText(
            "Hola " + username + ",\n\n" +
            "Has solicitado recuperar tu contraseña en FAST Distribuciones.\n\n" +
            "Tu código de verificación es:\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "        " + code + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Este código vence en 10 minutos.\n\n" +
            "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
            "Saludos,\n" +
            "Equipo FAST Distribuciones"
        );
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            // Don't throw exception to avoid blocking the flow
        }
    }

    /**
     * Enviar código de verificación para 2FA (login)
     */
    public void sendTwoFactorCode(String toEmail, String username, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🔐 Código de verificación de acceso - FAST");
        message.setText(
            "Hola " + username + ",\n\n" +
            "Se ha intentado iniciar sesión en tu cuenta de FAST Distribuciones.\n\n" +
            "Tu código de verificación es:\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "        " + code + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Este código vence en 5 minutos.\n\n" +
            "Si no fuiste tú, por favor contacta soporte inmediatamente.\n\n" +
            "Saludos,\n" +
            "Equipo FAST Distribuciones"
        );
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            // Don't throw exception to avoid blocking the flow
        }
    }

    /**
     * Enviar correo de Bienvenida
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("✅ Bienvenido a FAST Distribuciones");
        message.setText(
            "Hola " + username + ",\n\n" +
            "¡Bienvenido a FAST Distribuciones!\n\n" +
            "Tu cuenta ha sido creada exitosamente. Ahora puedes acceder a nuestro catálogo de productos B2B.\n\n" +
            "Si tienes alguna pregunta, no dudes en contactarnos.\n\n" +
            "Saludos,\n" +
            "Equipo FAST Distribuciones"
        );
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
