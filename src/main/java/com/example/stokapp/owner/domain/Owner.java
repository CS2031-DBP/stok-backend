package com.example.stokapp.owner.domain;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "owners")
public class Owner extends User {

    @ManyToMany
    @Column(name = "suppliers")
    private List<Supplier> suppliers = new ArrayList<>();

    @OneToMany
    @Column(name = "sales")
    private List<Sale> sales = new ArrayList<>();


    @OneToMany
    @Column(name = "Inventories")
    private List<Inventory> inventory = new ArrayList<>();
}