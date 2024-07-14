package com.example.stokapp.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnualSalesReportEventWithGraphic {
    private Long ownerId;
    private String email;
    private int year;
}
