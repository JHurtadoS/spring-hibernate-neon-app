package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.service.EmailVerificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/verify")
    public String verifyEmail(@RequestParam String emailAddress) {
        return emailVerificationService.verifyEmail(emailAddress);
    }
}