package com.example.stokapp.inventory.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.domain.EmployeeService;
import com.example.stokapp.event.SendLowStockEmailEvent;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthImpl authImpl;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private void verifyOwnerOrEmployee(Long ownerId) {
        String currentEmail = authImpl.getCurrentEmail();
        if (currentEmail == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        boolean isOwner = authImpl.isOwnerResource(ownerId);

        // Si el owner no tiene empleados, solo verificar si es el owner y no lanzar excepción
        if (owner.getEmployees().isEmpty()) {
            if (isOwner) {
                return;
            } else {
                throw new UnauthorizeOperationException("Not allowed");
            }
        }

        // Verificar si es empleado solo si hay empleados
        boolean isEmployee = owner.getEmployees().stream()
                .anyMatch(employee -> employee.getEmail().equals(currentEmail));

        if (!isOwner && !isEmployee) {
            throw new UnauthorizeOperationException("Not allowed");
        }
    }

    public void createInventory(Long ownerId, Long productId, Integer quantity) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStock(quantity);
        inventory.setOwner(owner);

        owner.getInventory().add(inventory);

        ownerRepository.save(owner);
        inventoryRepository.save(inventory);
    }

    public void reduceInventory(Long ownerId, Long inventoryId, Integer quantity) {
        verifyOwnerOrEmployee(ownerId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);

        if (inventory.getStock() < 5) {
            applicationEventPublisher.publishEvent(new SendLowStockEmailEvent(this, inventory.getOwner().getEmail(), inventory));
        }
    }

    public void increaseInventory(Long ownerId, Long inventoryId, Integer quantity) {
        verifyOwnerOrEmployee(ownerId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setStock(inventory.getStock() + quantity);
        inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long ownerId, Long inventoryId) {
        verifyOwnerOrEmployee(ownerId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getInventory().remove(inventory);

        ownerRepository.save(owner);
        inventoryRepository.delete(inventory);
    }

    public List<InventoryDto> findAll(Long ownerId) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<Inventory> inventories = owner.getInventory();
        return inventories.stream()
                .map(inventory -> {
                    InventoryDto inventoryDto = mapper.map(inventory, InventoryDto.class);
                    ProductDto productDto = mapper.map(inventory.getProduct(), ProductDto.class);
                    inventoryDto.setProduct(productDto);
                    return inventoryDto;
                })
                .collect(Collectors.toList());
    }

    public InventoryDto getInventoryByProductName(Long ownerId, String nombre) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Inventory inventory = owner.getInventory().stream()
                .filter(inv -> inv.getProduct().getName().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));

        InventoryDto inventoryDto = mapper.map(inventory, InventoryDto.class);
        ProductDto productDto = mapper.map(inventory.getProduct(), ProductDto.class);
        inventoryDto.setProduct(productDto);

        return inventoryDto;
    }
}
