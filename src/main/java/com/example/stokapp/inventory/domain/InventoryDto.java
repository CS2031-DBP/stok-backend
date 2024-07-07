package com.example.stokapp.inventory.domain;

import com.example.stokapp.product.domain.ProductDto;
import lombok.Data;

@Data
public class InventoryDto {
    private Long id;
    private ProductDto product;
    private int stock;
}