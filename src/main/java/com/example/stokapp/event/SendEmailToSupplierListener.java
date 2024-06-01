package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class SendEmailToSupplierListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    ProductRepository productRepository;

    @EventListener
    @Async
    public void handleRideCreated(SendEmailToSupplierEvent event){
        Product product = productRepository.findById(event.getProduct().getId()).orElse(null);
        String message = "Producto bajo de stock:" + "\n Detalles del Producto:" + product.getName();
        emailService.sendEmail(event.getEmail(), "PRODUCTO BAJO DE STOCK", message);

    }
}
