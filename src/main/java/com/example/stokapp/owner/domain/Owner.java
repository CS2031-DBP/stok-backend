package com.example.stokapp.owner.domain;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "owners")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Owner.class)
public class Owner extends User {

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Supplier> suppliers = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Sale> sales = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Inventory> inventory = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Employee> employees = new ArrayList<>();
}