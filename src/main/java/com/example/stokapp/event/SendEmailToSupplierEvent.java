package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.product.domain.Product;
import org.springframework.context.ApplicationEvent;

public class SendEmailToSupplierEvent extends ApplicationEvent {
    private String email;
    private Product product;
    private Long ownerId;
    private String message;

    public SendEmailToSupplierEvent(Object source, String email, Product product, Long ownerId, String message) {
        super(source);
        this.email = email;
        this.product = product;
        this.ownerId = ownerId;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public Product getProduct() {
        return product;
    }

    public Long getOwnerId(){return ownerId;}

    public String getMessage() {return message;}
}
