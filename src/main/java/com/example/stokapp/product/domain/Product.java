package com.example.stokapp.product.domain;

import com.example.stokapp.qr.domain.CodigoBarra;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "category", nullable = false)
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @Column(name = "qr_code", nullable = false)
    private CodigoBarra codigoBarra;

}