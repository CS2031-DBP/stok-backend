package com.example.stokapp.employee.domain;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "employees")
public class Employee extends User {

    @OneToMany
    @Column(name = "Inventories")
    @JsonBackReference
    private List<Inventory> inventory = new ArrayList<>();

    @OneToMany
    @Column(name = "sales")
    @JsonBackReference
    private List<Sale> sales = new ArrayList<>();

    @ManyToOne
    private Owner owner;
}
