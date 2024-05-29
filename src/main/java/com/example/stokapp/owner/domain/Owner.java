package com.example.stokapp.owner.domain;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "owners")
public class Owner extends User {

    @ManyToMany
    @JsonManagedReference
    private List<Supplier> suppliers = new ArrayList<>();

    @OneToMany
    @JsonManagedReference
    private List<Sale> sales = new ArrayList<>();


    @OneToMany
    @JsonManagedReference
    private List<Inventory> inventory = new ArrayList<>();

    @OneToMany
    @JsonManagedReference
    private List<Employee> employees = new ArrayList<>();
}