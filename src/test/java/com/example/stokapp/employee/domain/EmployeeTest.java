package com.example.stokapp.employee.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    private Employee employee;

    public void setUpEmployee() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@doe.com");
        employee.setPassword("123456");
    }

    @BeforeEach
    public void setUp() {
        setUpEmployee();
    }

    @Test
    void testEmployeeCreation(){
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john@doe.com", employee.getEmail());
        assertEquals("123456", employee.getPassword());
    }

    @Test
    void testEmployeeNull(){
        employee = null;
        assertNull(employee);
    }
