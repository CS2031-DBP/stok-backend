package com.example.stokapp.employee.domain;

import com.example.stokapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employees")
public class Employee extends User {

}
