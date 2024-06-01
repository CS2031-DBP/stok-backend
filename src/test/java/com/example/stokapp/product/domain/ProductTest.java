package com.example.stokapp.product.domain;

import com.example.stokapp.supplier.domain.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ProductTest {

    private Product product;
    private Supplier supplier;

    @BeforeEach
    public void setUp() {
        supplier = mock(Supplier.class);
        product = new Product();
        product.setId(1L);
        product.setName("Doritos");
        product.setDescription("Con extra queso");
        product.setPrice(2.50);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");
        product.setSupplier(supplier);
    }

    @Test
    public void testProductCreation() {
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Doritos", product.getName());
        assertEquals("Con extra queso", product.getDescription());
        assertEquals(2.50, product.getPrice());
        assertEquals(Category.Fritura, product.getCategory());
        assertEquals("QR12345", product.getQr());
        assertEquals(supplier, product.getSupplier());
    }

    @Test
    public void testSetSupplier() {
        Supplier newSupplier = mock(Supplier.class);
        product.setSupplier(newSupplier);

        assertNotNull(product.getSupplier());
        assertEquals(newSupplier, product.getSupplier());
    }

    @Test
    public void testRemoveSupplier() {
        product.setSupplier(null);

        assertNotNull(product);
        assertEquals(null, product.getSupplier());
    }
}
