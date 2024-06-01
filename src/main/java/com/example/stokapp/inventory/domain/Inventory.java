package com.example.stokapp.inventory.domain;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

@Entity
@Data
@Table(name = "inventories")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Inventory.class)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne(cascade = CascadeType.ALL)
    private Owner owner;

}