package com.example.stokapp.event;


import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SalesReportEvent extends ApplicationEvent {
    private Long ownerId;
    private String email;
    private Integer month;
    private Integer year;

    public SalesReportEvent(Object source, Long ownerId, String email, Integer month, Integer year) {
        super(source);
        this.ownerId = ownerId;
        this.email = email;
        this.month = month;
        this.year = year;
    }
}