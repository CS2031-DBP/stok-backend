package com.example.stokapp.sale.domain;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryforSaleDto;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SaleDto {

    @NotNull
    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private Integer amount;

    @NotNull
    private Double saleCant;

    @NotNull
    private InventoryforSaleDto inventoryforSaleDto;
}
