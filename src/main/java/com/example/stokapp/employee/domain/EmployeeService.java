package com.example.stokapp.employee.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public EmployeeResponseDto getEmployee(Long ownerId, Long employeeId) {
        verifyOwnerOrEmployee(ownerId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeResponseDto employeeResponseDto = mapper.map(employee, EmployeeResponseDto.class);

        if (employee.getOwner() != null) {
            OwnerResponseDto ownerResponseDto = mapper.map(employee.getOwner(), OwnerResponseDto.class);
            employeeResponseDto.setOwner(ownerResponseDto);
        }

        return employeeResponseDto;
    }

    @Transactional
    public void assignEmployeeToOwner(Long ownerId, Long employeeId) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        owner.getEmployees().add(employee);
        employee.setOwner(owner);
        employeeRepository.save(employee);
        ownerRepository.save(owner);
    }


    public void deleteEmployeeFromOwner(Long ownerId, Long employeeId) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        owner.getEmployees().remove(employee);

        ownerRepository.save(owner);
        employeeRepository.delete(employee);
    }

    public void updateEmployee(Long employeeId, UpdateEmployeeRequest updateEmployeeRequest) {
        if (!authImpl.isOwnerResource(employeeId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeToUpdate.setFirstName(updateEmployeeRequest.getFirstName());
        employeeToUpdate.setLastName(updateEmployeeRequest.getLastName());
        employeeToUpdate.setPhoneNumber(updateEmployeeRequest.getPhoneNumber());

        employeeRepository.save(employeeToUpdate);
    }

    private void verifyOwnerOrEmployee(Long ownerId) {
        String currentEmail = authImpl.getCurrentEmail();
        if (currentEmail == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        boolean isOwner = authImpl.isOwnerResource(ownerId);
        boolean isEmployee = owner.getEmployees().stream()
                .anyMatch(employee -> employee.getEmail().equals(currentEmail));

        if (!isOwner && !isEmployee) {
            throw new UnauthorizeOperationException("Not allowed");
        }
    }
}
