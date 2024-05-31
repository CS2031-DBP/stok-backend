package com.example.stokapp.inventory.domain;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InventoryTest {

    Inventory inventory = new Inventory();
    Product product = new Product();
    Owner owner = new Owner();

    @BeforeEach
    public void setUp(){
        inventory.setId(1L);
        inventory.setProduct(product);
        inventory.setStock(10);
        inventory.setOwner(owner);
    }

    @Test
    public void testInventoryCreation() {
        assertNotNull(inventory);
        assertNotNull(inventory.getProduct());
        assertNotNull(inventory.getOwner());
        assertEquals(1L, inventory.getId());
        assertEquals(10, inventory.getStock());
    }

    @Test
    public void testInventoryGettersAndSetters() {
        assertEquals(1L, inventory.getId());
        assertEquals(product, inventory.getProduct());
        assertEquals(10, inventory.getStock());
        assertEquals(owner, inventory.getOwner());
    }
}
