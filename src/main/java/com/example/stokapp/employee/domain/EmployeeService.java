package com.example.stokapp.employee.domain;

import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<Employee> getEmployee(Long id) {return employeeRepository.findById(id);}

    //SAVE EMPLOYEE
    public void createEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    //DELETE EMPLOYEE
    public void deleteEmployee(Long id) { employeeRepository.deleteById(id);}

    //UPDATE EMPLOYEE
    public void updateEmployee(Long id, Employee employee) {
        Employee employeeToUpdate = employeeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());
        employeeToUpdate.setEmail(employee.getEmail());
        employeeToUpdate.setPhoneNumber(employee.getPhoneNumber());

        employeeRepository.save(employeeToUpdate);

    }
}
