package com.example.stokapp.supplier.domain;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class SupplierTest {

    private Supplier supplier;
    private Owner owner;
    private List<Product> products;

    @BeforeEach
    public void setUp() {
        owner = mock(Owner.class);
        products = new ArrayList<>();
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setFirstName("John");
        supplier.setLastName("Doe");
        supplier.setEmail("john.doe@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setOwner(owner);
        supplier.setProducts(products);
    }

    @Test
    public void testSupplierCreation() {
        assertNotNull(supplier);
        assertEquals(1L, supplier.getId());
        assertEquals("John", supplier.getFirstName());
        assertEquals("Doe", supplier.getLastName());
        assertEquals("john.doe@example.com", supplier.getEmail());
        assertEquals("123456789", supplier.getPhoneNumber());
        assertEquals(owner, supplier.getOwner());
        assertEquals(products, supplier.getProducts());
    }

    @Test
    public void testAddProduct() {
        Product product = mock(Product.class);
        products.add(product);
        supplier.setProducts(products);

        assertNotNull(supplier.getProducts());
        assertEquals(1, supplier.getProducts().size());
        assertEquals(product, supplier.getProducts().get(0));
    }

    @Test
    public void testRemoveProduct() {
        Product product = mock(Product.class);
        products.add(product);
        supplier.setProducts(products);
        products.remove(product);
        supplier.setProducts(products);

        assertNotNull(supplier.getProducts());
        assertEquals(0, supplier.getProducts().size());
    }
}