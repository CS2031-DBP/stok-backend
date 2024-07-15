package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
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
public class SendEmailToSupplierListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @EventListener
    @Async
    public void handleRideCreated(SendEmailToSupplierEvent event){
        Product product = productRepository.findById(event.getProduct().getId()).orElse(null);
        Owner owner = ownerRepository.findById(event.getOwnerId()).orElseThrow(() -> new RuntimeException("Owner not found"));
        String message = event.getMessage();

        String htmlContent = loadTemplate("templates/lowStockEmailTemplate.html")
                .replace("{{ownerName}}", owner.getFirstName() + " " + owner.getLastName())
                .replace("{{ownerPhone}}", owner.getPhoneNumber())
                .replace("{{productName}}", product.getName())
                .replace("{{customMessage}}", message);

        try {
            emailService.sendHtmlEmail(event.getEmail(), "PRODUCTO BAJO DE STOCK", htmlContent, new ClassPathResource("static/stok_logo.png"));
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
