package com.example.stokapp.employee.domain;
import com.example.stokapp.user.domain.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
