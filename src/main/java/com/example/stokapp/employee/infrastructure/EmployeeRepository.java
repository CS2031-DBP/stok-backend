package com.example.stokapp.employee.infrastructure;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.user.infrastructure.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends UserRepository<Employee> {
}
