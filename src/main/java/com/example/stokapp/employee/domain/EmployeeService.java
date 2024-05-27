package com.example.stokapp.employee.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    AuthImpl authImpl;

    public Optional<Employee> getEmployee(Long id) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        return employeeRepository.findById(id);
    }

    //SAVE EMPLOYEE
    public void createEmployee(Employee employee) {
        if (!authImpl.isOwnerResource(employee.getId()))
            throw new UnauthorizeOperationException("Not allowed");
        employeeRepository.save(employee);
    }

    //DELETE EMPLOYEE
    public void deleteEmployee(Long id) {
        if (!authImpl.isOwnerResource(id))
            throw new UnauthorizeOperationException("Not allowed");

        employeeRepository.deleteById(id);}

    //UPDATE EMPLOYEE
    public void updateEmployee(Long id, Employee employee) {
        if (!authImpl.isOwnerResource(id))
            throw new UnauthorizeOperationException("Not allowed");

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
