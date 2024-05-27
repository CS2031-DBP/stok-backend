package com.example.stokapp.product.domain;

import com.example.stokapp.inventory.domain.Inventory;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ProductDto {

    private String name;

    private String description;

    private Double price;

    private Category category;

}