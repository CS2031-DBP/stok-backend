package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class SendLowStockEmailListener {
    @Autowired
    private EmailService emailService;

    @Autowired
    InventoryRepository inventoryRepository;

    @EventListener
    @Async
    public void sendLowStockEmailEvent(SendLowStockEmailEvent sendLowStockEmailEvent) {
        Inventory inventory = inventoryRepository.findById(sendLowStockEmailEvent.getInventory().getId()).orElse(null);

        String htmlContent = loadTemplate("templates/lowStockAlertTemplate.html")
                .replace("{{productName}}", inventory.getProduct().getName())
                .replace("{{stockQuantity}}", String.valueOf(inventory.getStock()));

        try {
            emailService.sendHtmlEmail(sendLowStockEmailEvent.getEmail(), "ALERTA PRODUCTO BAJO EN STOCK", htmlContent, new ClassPathResource("static/stok_logo.png"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String loadTemplate(String path) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}