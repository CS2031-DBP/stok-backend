package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
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

    @Autowired
    OwnerRepository ownerRepository;

    @EventListener
    @Async
    public void handleRideCreated(SendEmailToSupplierEvent event){
        Product product = productRepository.findById(event.getProduct().getId()).orElse(null);
        Owner owner = ownerRepository.findById(event.getOwnerId()).orElseThrow(() -> new RuntimeException("Owner not found"));
        String automessage = "Mensaje de : " + owner.getFirstName()+" "+ owner.getLastName() +  "\nNumero de telefono : " +  owner.getPhoneNumber()   +"\nProducto bajo de stock:" + "\n Detalles del Producto:" + product.getName();
        String message = event.getMessage();
        emailService.sendEmail(event.getEmail(), "PRODUCTO BAJO DE STOCK",  ""+ automessage + "\n" + message);

    }
}
