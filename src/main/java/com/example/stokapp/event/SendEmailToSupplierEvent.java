package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.product.domain.Product;
import org.springframework.context.ApplicationEvent;

public class SendEmailToSupplierEvent extends ApplicationEvent {
    private String email;
    private Product product;

    public SendEmailToSupplierEvent(Object source, String email, Product product) {
        super(source);
        this.email = email;
        this.product = product;
    }

    public String getEmail() {
        return email;
    }

    public Product getProduct() {
        return product;
    }
}
