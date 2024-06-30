package com.example.stokapp.employee.application;

import com.example.stokapp.employee.domain.*;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/{ownerId}/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long ownerId, @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(ownerId, employeeId));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDto> getDriver() {
        return ResponseEntity.ok(employeeService.getEmployeeOwnInfo());
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/assign/{ownerId}/{employeeId}")
    public ResponseEntity<String> assignEmployeeToOwner(@PathVariable Long ownerId, @PathVariable Long employeeId) {
        employeeService.assignEmployeeToOwner(ownerId, employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee assigned to owner");
    }

    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/delete/{ownerId}/{employeeId}")
    public ResponseEntity<Void> deleteEmployeeFromOwner(@PathVariable Long ownerId, @PathVariable Long employeeId) {
        employeeService.deleteEmployeeFromOwner(ownerId, employeeId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted");
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PatchMapping("/update/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        employeeService.updateEmployee(employeeId, updateEmployeeRequest);
        return ResponseEntity.ok("Employee updated");
    }
}