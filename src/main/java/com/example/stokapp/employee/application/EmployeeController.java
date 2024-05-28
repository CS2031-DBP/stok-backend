package com.example.stokapp.employee.application;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.domain.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {this.employeeService = employeeService;}

    @PostMapping("/create/{ownerId}")
    public ResponseEntity<String> addEmployee(@PathVariable Long ownerId, @RequestBody Employee employee) {
        employeeService.createEmployee(employee,ownerId);
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
