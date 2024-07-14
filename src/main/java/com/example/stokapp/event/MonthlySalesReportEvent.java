package com.example.stokapp.event;

import org.springframework.context.ApplicationEvent;

public class MonthlySalesReportEvent extends ApplicationEvent {
    private final Long ownerId;
    private final String email;
    private final int month;
    private final int year;

    public MonthlySalesReportEvent(Object source, Long ownerId, String email, int month, int year) {
        super(source);
        this.ownerId = ownerId;
        this.email = email;
        this.month = month;
        this.year = year;
    }

    // Getters
    public Long getOwnerId() {
        return ownerId;
    }

    public String getEmail() {
        return email;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}