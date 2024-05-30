package com.example.stokapp.product.domain;

import com.example.stokapp.supplier.domain.SupplierDto;
import com.example.stokapp.supplier.domain.SupplierWithNoProductDto;
import lombok.Data;

@Data
public class ProductWithSupplierDto {
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Category category;

    private SupplierWithNoProductDto supplier;
}
