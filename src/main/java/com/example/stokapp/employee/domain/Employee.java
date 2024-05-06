package com.example.stokapp.employee.domain;

import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
