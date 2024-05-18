package com.example.stokapp.owner.domain;

import com.example.stokapp.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    private Owner owner;

    public void setUpOwner(){
        owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
    }

    @BeforeEach
    public void setUp() {
        setUpOwner();
    }

    @Test
    void ownerCreation(){
        assertNotNull(owner);
        assertEquals("Bellido20@gmail.com", owner.getEmail());
        assertEquals("Jesus", owner.getFirstName());
        assertEquals("Bellido", owner.getLastName());
        assertEquals("992020202", owner.getPhoneNumber());
        assertEquals("c20a", owner.getPassword());
        assertEquals(Role.OWNER, owner.getRole());
    }

    @Test
    void ownerRole(){
        assertEquals(Role.OWNER, owner.getRole());
    }

    @Test
    void ownerNull(){
        owner = null;
        assertNull(owner);
    }
}
