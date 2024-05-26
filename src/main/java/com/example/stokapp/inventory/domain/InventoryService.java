package com.example.stokapp.inventory.domain;

import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;


    // CREAR INVENTARIO
    public void createInventory(Inventory inventory) {
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

        if (inventory.getStock() < 5) {
            sendLowStockAlert(inventory);
        }
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

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    private void sendLowStockAlert(Inventory inventory) {
        System.out.println("Advertencia: El producto " + inventory.getProduct().getName() + " se está acabando pronto. Stock actual: " + inventory.getStock());

    }

    public InventoryDto getInventoryByProductName(String nombre) {
        Inventory inventory = inventoryRepository.findInventoryByProductName(nombre);
        if (inventory == null) {
            throw new RuntimeException("Product not found");
        }
        InventoryDto inventoryDto = mapper.map(inventory, InventoryDto.class);
        ProductDto productDto = mapper.map(inventory.getProduct(), ProductDto.class);
        inventoryDto.setProduct(productDto);

        return inventoryDto;
    }
}