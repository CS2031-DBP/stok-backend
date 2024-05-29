package com.example.stokapp.sale.domain;

import lombok.Data;

@Data
public class CreateSaleRequest {
    private Long ownerId;
    private Long inventoryId;
    private Integer amount;
}
