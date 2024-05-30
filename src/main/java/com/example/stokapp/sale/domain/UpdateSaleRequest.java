package com.example.stokapp.sale.domain;

import lombok.Data;

@Data
public class UpdateSaleRequest {
    private Long ownerId;
    private Long saleId;
    private Integer newAmount;
}
