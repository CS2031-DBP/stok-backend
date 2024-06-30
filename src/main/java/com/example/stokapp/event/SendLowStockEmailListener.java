package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.configuration.PdfGenerator;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class SendLowStockEmailListener {
    @Autowired
    private EmailService emailService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @EventListener
    @Async
    public void sendLowStockEmailEvent(SendLowStockEmailEvent sendLowStockEmailEvent) {
        Inventory inventory = inventoryRepository.findById(sendLowStockEmailEvent.getInventory().getId()).orElse(null);
        if (inventory == null) {
            throw new RuntimeException("Inventory not found");
        }

        String email = sendLowStockEmailEvent.getEmail();
        String subject = "ALERTA PRODUCTO BAJO EN STOCK";
        String text = "El siguiente producto presenta un nivel bajo de stock: " + inventory.getProduct().getName() +
                "\nCantidad de stock disponible: " + inventory.getStock();

        try {
            // Generar el archivo PDF
            File pdfFile = PdfGenerator.generatePdfLowStock("LowStock", email, inventory);

            // Enviar el email con el PDF adjunto
            emailService.sendEmailWithAttachment(email, subject, text, pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
