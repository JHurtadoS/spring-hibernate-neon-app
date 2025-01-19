package com.example.springhibernateneonapp.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
public class EmailVerificationService {

    private final SesClient sesClient;

    public EmailVerificationService() {
        // Inicializa el cliente SES con la región adecuada
        this.sesClient = SesClient.builder()
                .region(Region.US_EAST_1) // Cambia a la región donde configuraste SES
                .build();
    }

    public String verifyEmail(String emailAddress) {
        try {
            // Crear la solicitud de verificación
            VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                    .emailAddress(emailAddress)
                    .build();

            // Enviar la solicitud a SES
            sesClient.verifyEmailIdentity(request);

            return "Se envió una solicitud de verificación al correo: " + emailAddress;
        } catch (SesException e) {
            e.printStackTrace();
            return "Error al intentar verificar el correo: " + e.getMessage();
        }
    }
}