package com.example.stokapp.inventory.application;

import com.example.stokapp.inventory.domain.CreateInventoryRequest;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.inventory.domain.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // GET INVENTARIO para un owner específico
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<InventoryDto>> getAll(@PathVariable Long ownerId) {
        return ResponseEntity.ok(inventoryService.findAll(ownerId));
    }

    // CREAR INVENTARIO
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody CreateInventoryRequest request) {
        inventoryService.createInventory(request.getOwnerId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory created successfully");
    }

    // GET INVENTORY BY PRODUCT NAME para un owner específico
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/find/{ownerId}")
    public ResponseEntity<InventoryDto> findProductByName(@PathVariable Long ownerId, @RequestParam String productName) {
        InventoryDto inventory = inventoryService.getInventoryByProductName(ownerId, productName);
        return ResponseEntity.ok(inventory);
    }

    // REDUCIR STOCK
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PatchMapping("/{ownerId}/{inventoryId}/reduce/{quantity}")
    public ResponseEntity<String> reduceInventory(@PathVariable Long ownerId , @PathVariable Long inventoryId, @PathVariable Integer quantity) {
        inventoryService.reduceInventory(ownerId, inventoryId, quantity);
        return ResponseEntity.ok("Stock reduced successfully");
    }

    // AUMENTAR STOCK
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PatchMapping("/{ownerId}/{inventoryId}/increase/{quantity}")
    public ResponseEntity<String> increaseInventory(@PathVariable Long ownerId , @PathVariable Long inventoryId, @PathVariable Integer quantity) {
        inventoryService.increaseInventory(ownerId, inventoryId, quantity);
        return ResponseEntity.ok("Stock increased successfully");
    }

    // ELIMINAR INVENTARIO
    @DeleteMapping("/delete/{inventoryId}/{ownerId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<String> deleteInventory(@PathVariable Long ownerId, @PathVariable Long inventoryId) {
        inventoryService.deleteInventory(ownerId, inventoryId);
        return ResponseEntity.ok("Inventory deleted successfully");
    }
}
