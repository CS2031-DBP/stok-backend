package com.example.stokapp.owner.domain;

import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Owner extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column(name = "suppliers", nullable = false)
    private List<Supplier> suppliers;
}