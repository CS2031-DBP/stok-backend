package com.example.stokapp.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    public void setUpProducts(){
        product = new Product();
        product.setName("Oreo");
        product.setDescription("Galleta de chocolate rellenas de crema");
        product.setPrice(1.70);
        product.setCategory(Category.Galleta);
    }

    @BeforeEach
    public void setUp(){
        setUpProducts();
    }

    @Test
    public void testProductCreated(){
        assertNotNull(product);
        assertEquals("Oreo", product.getName());
        assertEquals("Galleta de chocolate rellenas de crema", product.getDescription());
        assertEquals(1.70, product.getPrice());
        assertEquals(Category.Galleta, product.getCategory());
    }

    @Test
    public void testProductCategory(){
        assertEquals(Category.Galleta, product.getCategory());
        product.setCategory(Category.Dulce);
        assertEquals(Category.Dulce, product.getCategory());
        product.setCategory(Category.Chocolate);
        assertEquals(Category.Chocolate, product.getCategory());
        product.setCategory(Category.Bebida);
        assertEquals(Category.Bebida, product.getCategory());
    }

    @Test
    public void testProductNull(){
        product = null;
        assertNull(product);
    }
}
