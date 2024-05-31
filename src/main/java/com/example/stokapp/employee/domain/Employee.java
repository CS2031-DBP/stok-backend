package com.example.stokapp.employee.domain;

import com.example.stokapp.owner.domain.Owner;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "employees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Employee.class)
@EqualsAndHashCode(callSuper=false)
public class Employee extends User {

    @ManyToOne
    private Owner owner;
}
