package com.example.stokapp.inventory.domain;

import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory;

    private Inventory inventory1;

    private Product product;

    private Product product1;

    public void setUpProduct(){
        product = new Product();
        product.setName("Oreo");
        product.setDescription("Galleta de chocolate rellenas de crema");
        product.setPrice(1.70);
        product.setCategory(Category.Galleta);

        product1 = new Product();
        product1.setName("CocaCola");
        product1.setDescription("Deliciosa bebida sabor jarabe");
        product1.setPrice(2.50);
        product1.setCategory(Category.Bebida);
    }

    public void setUpInventory(){
        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStock(6);

        inventory1 = new Inventory();
        inventory1.setProduct(product1);
        inventory1.setStock(10);
    }

    @BeforeEach
    public void setUp(){
        setUpProduct();
        setUpInventory();
    }

    @Test
    void testInventoryCreated(){
        assertNotNull(inventory);
        assertNotNull(inventory.getProduct());
        assertNotNull(inventory.getStock());
    }

    @Test
    void testInventoryCreatedOther(){
        assertNotNull(inventory1);
        assertNotNull(inventory1.getProduct());
        assertNotNull(inventory1.getStock());
    }

    @Test
    void testInventoryNull(){
        inventory = null;
        assertNull(inventory);
    }

