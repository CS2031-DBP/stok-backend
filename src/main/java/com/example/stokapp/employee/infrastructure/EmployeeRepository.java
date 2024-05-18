package com.example.stokapp.employee.infrastructure;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.user.infrastructure.UserRepository;

public interface EmployeeRepository extends UserRepository<Employee> {
}
