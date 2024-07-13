package com.example.stokapp.sale.domain;

import lombok.Data;

@Data
public class AnnualSalesReportRequest {
    private Long ownerId;
    private String email;
    private int year;
}