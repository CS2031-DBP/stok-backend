package com.example.stokapp.supplier.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {

    private Supplier supplier;

    public void setUpSupplier(){
        supplier = new Supplier();
        supplier.setFirstName("Ramiro");
        supplier.setLastName("Gutierres");
        supplier.setEmail("ramiro.gutierres@gmail.com");
        supplier.setPhoneNumber("1234567890");
    }

    @BeforeEach
    public void setUp(){
        setUpSupplier();
    }

    @Test
    public void testSupplierCreated(){
        assertNotNull(supplier);
        assertEquals("Ramiro", supplier.getFirstName());
        assertEquals("Gutierres", supplier.getLastName());
        assertEquals("ramiro.gutierres@gmail.com", supplier.getEmail());
        assertEquals("1234567890", supplier.getPhoneNumber());
    }

    @Test
    public void testSupplierNull(){
        supplier = null;
        assertNull(supplier);
    }
}
