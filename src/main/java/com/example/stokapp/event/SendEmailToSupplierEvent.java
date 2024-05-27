package com.example.stokapp.event;

import com.example.stokapp.inventory.domain.Inventory;
import org.springframework.context.ApplicationEvent;

public class SendEmailToSupplierEvent extends ApplicationEvent {
    private String email;
    private Inventory inventory;

    public SendEmailToSupplierEvent(Object source, String email, Inventory inventory) {
        super(source);
        this.email = email;
        this.inventory = inventory;
    }

    public String getEmail() {
        return email;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
