package com.example.stokapp.owner.domain;

import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "owners")
public class Owner extends User {

    @OneToMany
    @Column(name = "suppliers")
    private List<Supplier> suppliers;

    @OneToMany
    @Column(name = "suppliers")
    private List<Sale> sales;
}