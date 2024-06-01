package com.example.stokapp.employee.domain;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.sale.domain.Sale;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.example.stokapp.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "employees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Employee.class)
public class Employee extends User {

    @ManyToOne
    private Owner owner;
}
