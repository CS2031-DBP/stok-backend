package com.example.stokapp.employee.domain;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    private Employee employee;

    public void setUpEmployee(){
        employee = new Employee();
        employee.setEmail("mateoNoel@gmail.com");
        employee.setFirstName("Mateo");
        employee.setLastName("Noel");
        employee.setPhoneNumber("123456789");
        employee.setCreatedAt(ZonedDateTime.now());
        employee.setPassword("17Proyecto");
        employee.setRole(Role.EMPLOYEE);
    }

    @BeforeEach
    public void setUp() {
        setUpEmployee();
    }

    @Test
    void employeeCreation(){
        assertNotNull(employee);
        assertEquals("mateoNoel@gmail.com", employee.getEmail());
        assertEquals("Mateo", employee.getFirstName());
        assertEquals("Noel", employee.getLastName());
        assertEquals("123456789", employee.getPhoneNumber());
        assertEquals("17Proyecto", employee.getPassword());
        assertEquals(Role.EMPLOYEE, employee.getRole());
        assertNotEquals(Role.OWNER, employee.getRole());
    }

    @Test
    void employeeRole(){
        assertEquals(Role.EMPLOYEE, employee.getRole());
    }

    @Test
    void employeeNull(){
        employee = null;
        assertNull(employee);
    }
}
