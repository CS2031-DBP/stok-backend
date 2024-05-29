package com.example.stokapp.inventory.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.domain.EmployeeService;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.event.SendEmailToSupplierEvent;
import com.example.stokapp.event.SendLowStockEmailEvent;
import com.example.stokapp.event.WelcomeEmailEvent;
import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    AuthImpl authImpl;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private EmployeeService employeeService;


    // CREAR INVENTARIO
    public void createInventory(Long ownerId, Long productId, Integer quantity) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

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
            applicationEventPublisher.publishEvent(new SendLowStockEmailEvent(this, inventory.getOwner().getEmail(), inventory));
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
    public void deleteInventory(Long ownerId, Long inventoryId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getInventory().remove(inventory);


        ownerRepository.save(owner);
        inventoryRepository.delete(inventory);
    }


    // FIND ALL INVENTORY WITH DTO //modificar que sea de un owner
    public List<InventoryDto> findAll() {
        String username = authImpl.getCurrentEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream()
                .map(inventory -> {
                    InventoryDto inventoryDto = mapper.map(inventory, InventoryDto.class);
                    ProductDto productDto = mapper.map(inventory.getProduct(), ProductDto.class);
                    inventoryDto.setProduct(productDto);
                    return inventoryDto;
                })
                .collect(Collectors.toList());
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