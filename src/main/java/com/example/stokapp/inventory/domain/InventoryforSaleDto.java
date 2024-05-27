package com.example.stokapp.inventory.domain;

import com.example.stokapp.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryforSaleDto {
    @NotNull
    private Long id;

    @NotNull
    private Product product;

}
