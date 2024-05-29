package com.example.stokapp.inventory.domain;

import lombok.Data;

@Data
public class CreateInventoryRequest {
    private Long ownerId;
    private Long productId;
    private Integer quantity;
}
