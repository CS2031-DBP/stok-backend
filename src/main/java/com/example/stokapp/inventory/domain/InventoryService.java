package com.example.stokapp.inventory.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.event.SendEmailToSupplierEvent;
import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    @Autowired
    AuthImpl authImpl;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    // CREAR INVENTARIO
    public void createInventory(Inventory inventory) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        inventoryRepository.save(inventory);}

    // REDUCIR STOCK
    public void reduceInventory(Long inventoryId, Integer quantity) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

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
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setStock(inventory.getStock() + quantity);
        inventoryRepository.save(inventory);
    }

    // ELIMINAR INVENTARIO
    public void deleteInventory(Long inventoryId) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryRepository.delete(inventory);
    }

    public List<Inventory> findAll() {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        return inventoryRepository.findAll();
    }

    private void sendLowStockAlert(Inventory inventory) {
        System.out.println("Advertencia: El producto " + inventory.getProduct().getName() + " se está acabando pronto. Stock actual: " + inventory.getStock());

    }

    public InventoryDto getInventoryByProductName(String nombre) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

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