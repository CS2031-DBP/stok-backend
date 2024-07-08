package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WelcomeEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void sendWelcomeEmail(WelcomeEmailEvent welcomeEmailEvent) {
        emailService.sendEmail(welcomeEmailEvent.getEmail(), "Bienvenido", "Hola " + welcomeEmailEvent.getName() + ", bienvenido a nuestra plataforma. EN CASO SEAS UN TRABAJADOR, DALE TU ID A TU JEFE, EN CASO SEAS UN JEFE NO COMPARTAS TU ID. Este es tu ID: " + welcomeEmailEvent.getId().toString());
    }
}