package com.example.stokapp.inventory.domain;

import com.example.stokapp.inventory.infrastructure.InventroyRepository;
import com.example.stokapp.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventroyRepository inventoryRepository;


    // CREAR INVENTARIO
    public void createInventory(Inventory inventory, Product product) {
        inventory.setProduct(product);
        inventoryRepository.save(inventory);}

    // REDUCIR STOCK
    public void reduceInventory(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
    }

    // AUMENTAR SOTCK
    public void increaseInventory(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setStock(inventory.getStock() + quantity);
        inventoryRepository.save(inventory);
    }

    // ELIMINAR INVENTARIO
    public void deleteInventory(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryRepository.delete(inventory);
    }
}
