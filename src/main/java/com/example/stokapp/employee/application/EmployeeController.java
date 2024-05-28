package com.example.stokapp.employee.application;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.domain.EmployeeResponseDto;
import com.example.stokapp.employee.domain.EmployeeService;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {this.employeeService = employeeService;}

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }

    @PostMapping("/create/{ownerId}/{employeeId}")
    public ResponseEntity<String> addEmployee(@PathVariable Long ownerId, @PathVariable Long employeeId) {
        employeeService.createEmployee(employeeId,ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created");
    }

    @DeleteMapping("/delete/{ownerId}/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long ownerId, @PathVariable Long employeeId) {
        employeeService.deleteEmployee(ownerId,employeeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok("Employee updated");
    }


}
