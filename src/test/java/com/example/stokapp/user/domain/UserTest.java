package com.example.stokapp.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    public void setUpUser(){
        user = new User();
        user.setRole(Role.OWNER);
        user.setFirstName("Sebastian");
        user.setLastName("Caycho");
        user.setEmail("sebastian.caycho@gmail.com");
        user.setPassword("123456");
        user.setPhoneNumber("987654321");
    }

    @BeforeEach
    public void setUp(){
        setUpUser();
    }

    @Test
    public void testUserCreated(){
        assertNotNull(user);
        assertEquals("Sebastian", user.getFirstName());
        assertEquals("Caycho", user.getLastName());
        assertEquals("sebastian.caycho@gmail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals("987654321", user.getPhoneNumber());
    }

    @Test
    public void testUserRole(){
        assertEquals(Role.OWNER, user.getRole());
        user.setRole(Role.EMPLOYEE);
        assertEquals(Role.EMPLOYEE, user.getRole());
    }

    @Test
    public void testUserNull(){
        user = null;
        assertNull(user);
    }
}
