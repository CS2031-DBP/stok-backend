package com.example.stokapp.event;

import org.springframework.context.ApplicationEvent;

public class WelcomeEmailEvent extends ApplicationEvent {
    private String email;
    private String name;
    private Long id;

    public WelcomeEmailEvent(Object source, String email, String name, Long id) {
        super(source);
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}