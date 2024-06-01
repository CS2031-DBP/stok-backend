package com.example.stokapp.event;

import org.springframework.context.ApplicationEvent;

public class WelcomeEmailEvent extends ApplicationEvent {
    private String email;
    private String name;

    public WelcomeEmailEvent(Object source, String email, String name) {
        super(source);
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
