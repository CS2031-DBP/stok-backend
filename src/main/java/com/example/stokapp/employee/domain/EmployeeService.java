package com.example.stokapp.employee.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AuthImpl authImpl;

    @Autowired
    private ModelMapper mapper;

    // GET EMPLOYEE
    public EmployeeResponseDto getEmployee(Long ownerId, Long employeeId) {
        String username = authImpl.getCurrentEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employee.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed to access this employee");
        }

        EmployeeResponseDto employeeResponseDto = mapper.map(employee, EmployeeResponseDto.class);

        if (employee.getOwner() != null) {
            OwnerResponseDto ownerResponseDto = mapper.map(employee.getOwner(), OwnerResponseDto.class);
            employeeResponseDto.setOwner(ownerResponseDto);
        }

        return employeeResponseDto;
    }

    // ASSIGN EMPLOYEE TO OWNER
    public void assignEmployeeToOwner(Long ownerId, Long employeeId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        owner.getEmployees().add(employee);
        employee.setOwner(owner);
        employeeRepository.save(employee);
    }

    // DELETE EMPLOYEE FROM OWNER
    public void deleteEmployeeFromOwner(Long ownerId, Long employeeId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employee.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed to access this employee");
        }

        owner.getEmployees().remove(employee);
        employee.setOwner(null);
        employeeRepository.save(employee);
    }

    // UPDATE EMPLOYEE
    public void updateEmployee(Long employeeId, UpdateEmployeeRequest updatedEmployeeRequest) {
        if (!authImpl.isOwnerResource(employeeId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeToUpdate.setFirstName(updatedEmployeeRequest.getFirstName());
        employeeToUpdate.setLastName(updatedEmployeeRequest.getLastName());
        employeeToUpdate.setPhoneNumber(updatedEmployeeRequest.getPhoneNumber());

        employeeRepository.save(employeeToUpdate);
    }
}
