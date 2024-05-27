package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.configuration.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class SendEmailToSupplierListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    InventoryRepository inventoryRepository;

    @EventListener
    @Async
    public void handleRideCreated(SendEmailToSupplierEvent event){
        Inventory inventory = inventoryRepository.findById(event.getInventory().getId()).orElse(null);
        String message = "Producto bajo de stock:" + "\n Detalles del Producto:" + inventory.getProduct().getName();
        emailService.sendEmail(event.getEmail(), "PRODUCTO BAJO DE STOCK", message);

    }
}
