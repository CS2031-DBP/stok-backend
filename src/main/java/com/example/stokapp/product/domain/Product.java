package com.example.stokapp.product.domain;

import com.example.stokapp.qr.domain.QR;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
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

    @OneToOne //(mappedBy = "qr_code", cascade = CascadeType.ALL)
    //@Column(name = "qr_code", nullable = false) //con este no corre
    @PrimaryKeyJoinColumn //-- con este sí corre
    private QR qr;
}