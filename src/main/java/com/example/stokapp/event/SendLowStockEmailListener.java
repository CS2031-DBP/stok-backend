package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
        emailService.sendEmail(sendLowStockEmailEvent.getEmail(), "ALERTA PRODUCTO BAJO EN STOCK", "El siguiente producto presenta un nivel bajo de stock: " + inventory.getProduct().getName() + " " + "\n Cantidad de stock disponible: " + inventory.getStock());
    }
}
