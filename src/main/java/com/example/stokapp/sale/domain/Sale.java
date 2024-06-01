package com.example.stokapp.sale.domain;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "sales")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Sale.class)
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "saleCant")
    private Double saleCant;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
}