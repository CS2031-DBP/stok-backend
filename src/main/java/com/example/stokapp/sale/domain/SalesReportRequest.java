package com.example.stokapp.sale.domain;

import lombok.Data;

@Data
public class SalesReportRequest {
    private Long ownerId;
    private String email;
    private Integer month;
    private Integer year;
}