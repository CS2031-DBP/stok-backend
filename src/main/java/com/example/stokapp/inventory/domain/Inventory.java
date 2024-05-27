package com.example.stokapp.inventory.domain;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@Column(name = "product", nullable = false) --con este no corre
    //@PrimaryKeyJoinColumn --con este sí corre
    private Product product;

    @Column(name = "stock", nullable = false)
    private int stock;

    @ManyToOne
    @Column(name = "Owner")
    private Owner owner;

    @ManyToOne
    @Column(name = "Employee")
    private Employee employee;
}