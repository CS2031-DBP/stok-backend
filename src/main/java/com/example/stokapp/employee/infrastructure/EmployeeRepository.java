package com.example.stokapp.employee.infrastructure;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.user.infrastructure.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeRepository extends UserRepository<Employee> {

    Page<Employee> findAllByOwner(Owner owner, Pageable pageable);
}
