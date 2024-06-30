package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.configuration.PdfGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class WelcomeEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void sendWelcomeEmail(WelcomeEmailEvent welcomeEmailEvent) {
        String email = welcomeEmailEvent.getEmail();
        String name = welcomeEmailEvent.getName();
        String subject = "Bienvenido";
        String text = "Hola " + name + ", bienvenido a nuestra plataforma.";

        try {
            // Generar el archivo PDF
            File pdfFile = PdfGenerator.generatePdf("welcome_" + name + ".pdf", email, name);

            // Enviar el email con el PDF adjunto
            emailService.sendEmailWithAttachment(email, subject, text, pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
