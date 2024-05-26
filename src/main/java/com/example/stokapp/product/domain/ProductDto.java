package com.example.stokapp.product.domain;
import com.example.stokapp.inventory.domain.Inventory;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ProductDto {

    private ZonedDateTime createdAt;

    private String productName;

    private Double productPrice;

    private Integer amount;

    private Inventory inventory;

}
