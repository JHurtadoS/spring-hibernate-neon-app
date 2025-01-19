package com.example.springhibernateneonapp.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile file) throws Exception {
        // Crear un mensaje MIME para el correo principal
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Configurar los detalles del correo principal
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("juaneshs2014@gmail.com"); // Cambia al remitente verificado en SES

        // Adjuntar el archivo recibido como MultipartFile
        if (file != null && !file.isEmpty()) {
            helper.addAttachment(file.getOriginalFilename(), file);
        }

        // Enviar el correo principal
        mailSender.send(message);
        System.out.println("Correo enviado exitosamente con archivo adjunto a: " + toEmail);

        // Enviar la notificación
        sendNotificationEmail(toEmail, subject);
    }

    private void sendNotificationEmail(String originalRecipient, String originalSubject) throws Exception {
        // Crear un mensaje MIME para la notificación
        MimeMessage notificationMessage = mailSender.createMimeMessage();
        MimeMessageHelper notificationHelper = new MimeMessageHelper(notificationMessage, true);

        // Configurar los detalles del correo de notificación
        notificationHelper.setTo("juaneshs2014@gmail.com"); // Dirección de correo donde llegará la notificación
        notificationHelper.setSubject("Correo enviado: " + originalSubject);
        notificationHelper.setText(
                "Se ha enviado un correo a: " + originalRecipient + "\n" +
                        "Asunto: " + originalSubject + "\n" +
                        "Fecha y hora: " + java.time.LocalDateTime.now(),
                false
        );
        notificationHelper.setFrom("juaneshs2014@gmail.com");

        // Enviar el correo de notificación
        mailSender.send(notificationMessage);
        System.out.println("Notificación enviada exitosamente.");
    }
}
