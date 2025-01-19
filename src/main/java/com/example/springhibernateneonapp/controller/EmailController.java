package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.service.EmailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-with-file")
    public String sendEmailWithFile(
            @RequestParam String toEmail,
            @RequestParam String subject,
            @RequestParam String body,
                @RequestParam("file") MultipartFile file) {
        try {
            emailService.sendEmailWithAttachment(toEmail, subject, body, file);
            return "Correo enviado exitosamente con adjunto.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}