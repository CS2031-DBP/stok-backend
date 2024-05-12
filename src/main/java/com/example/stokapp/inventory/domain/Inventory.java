package com.example.stokapp.inventory.domain;

import com.example.stokapp.product.domain.Product;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@Column(name = "product", nullable = false)
    @PrimaryKeyJoinColumn
    private Product product;

    @Column(name = "stock", nullable = false)
    private int stock;
}