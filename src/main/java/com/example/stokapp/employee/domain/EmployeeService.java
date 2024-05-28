package com.example.stokapp.employee.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    AuthImpl authImpl;

    public Optional<Employee> getEmployee(Long ownerId, Long employeeId) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        return employeeRepository.findById(employeeId);
    }

    //SAVE EMPLOYEE
    public void createEmployee(Employee employee, Long ownerId) {
        if (!authImpl.isOwnerResource(ownerId) && !authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getEmployees().add(employee);

        employeeRepository.save(employee);
    }

    //DELETE EMPLOYEE
    public void deleteEmployee(Long ownerId , Long employeeId) {
        if (!authImpl.isOwnerResource(ownerId) && !authImpl.isOwnerResource(employeeId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        owner.getEmployees().remove(employee);
        employeeRepository.deleteById(employeeId);
    }

    //UPDATE EMPLOYEE
    public void updateEmployee(Long employeeId, Employee employeeNuevo) {
        if (!authImpl.isOwnerResource(employeeId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        Employee employeeToUpdate = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeToUpdate.setFirstName(employeeNuevo.getFirstName());
        employeeToUpdate.setLastName(employeeNuevo.getLastName());
        employeeToUpdate.setEmail(employeeNuevo.getEmail());
        employeeToUpdate.setPhoneNumber(employeeNuevo.getPhoneNumber());

        employeeRepository.save(employeeToUpdate);

    }
}
