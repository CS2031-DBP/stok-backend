package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Component
public class WelcomeEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void sendWelcomeEmail(WelcomeEmailEvent welcomeEmailEvent) {
        String templatePath = "templates/welcomeEmailTemplate.html";
        String content = loadTemplate(templatePath)
                .replace("{{name}}", welcomeEmailEvent.getName())
                .replace("{{id}}", welcomeEmailEvent.getId().toString());

        try {
            emailService.sendHtmlEmail(welcomeEmailEvent.getEmail(), "Bienvenido a ST★K", content, new ClassPathResource("static/stok_logo.png"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String loadTemplate(String path) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path)))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}