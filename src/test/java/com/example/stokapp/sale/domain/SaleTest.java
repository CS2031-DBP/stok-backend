package com.example.stokapp.sale.domain;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class SaleTest {

    private Sale sale;
    private Inventory inventory;
    private Owner owner;

    @BeforeEach
    public void setUp() {
        inventory = mock(Inventory.class);
        owner = mock(Owner.class);

        sale = new Sale();
        sale.setId(1L);
        sale.setCreatedAt(ZonedDateTime.now());
        sale.setAmount(5);
        sale.setSaleCant(250.00);
        sale.setInventory(inventory);
        sale.setOwner(owner);
    }

    @Test
    public void testSaleCreation() {
        assertNotNull(sale);
        assertEquals(1L, sale.getId());
        assertNotNull(sale.getCreatedAt());
        assertEquals(5, sale.getAmount());
        assertEquals(250.00, sale.getSaleCant());
        assertEquals(inventory, sale.getInventory());
        assertEquals(owner, sale.getOwner());
    }

    @Test
    public void testSetInventory() {
        Inventory newInventory = mock(Inventory.class);
        sale.setInventory(newInventory);

        assertNotNull(sale.getInventory());
        assertEquals(newInventory, sale.getInventory());
    }

    @Test
    public void testRemoveInventory() {
        sale.setInventory(null);

        assertNotNull(sale);
        assertEquals(null, sale.getInventory());
    }

    @Test
    public void testSetOwner() {
        Owner newOwner = mock(Owner.class);
        sale.setOwner(newOwner);

        assertNotNull(sale.getOwner());
        assertEquals(newOwner, sale.getOwner());
    }

}
