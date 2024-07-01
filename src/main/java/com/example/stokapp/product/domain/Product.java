package com.example.stokapp.product.domain;

import com.example.stokapp.qr.domain.QR;
import com.example.stokapp.supplier.domain.Supplier;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Product.class)
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

    @Column(name = "qr", nullable = true)
    @Nullable
    private String qr;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @Nullable
    private Supplier supplier;
}