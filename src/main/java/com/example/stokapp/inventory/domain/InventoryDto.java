package com.example.stokapp.inventory.domain;

import com.example.stokapp.product.domain.ProductDto;
import lombok.Data;

@Data
public class InventoryDto {
    private ProductDto product;
    private int stock;
}