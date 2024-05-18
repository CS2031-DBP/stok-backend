package com.example.stokapp.inventory.application;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // CREAR INVENTARIO
    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody Inventory inventory, @RequestBody Product product) {
        inventoryService.createInventory(inventory, product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory created successfully");
    }

    // REDUCIR STOCK
    @PatchMapping("/{inventoryId}/reduce/{quantity}")
    public ResponseEntity<String> reduceInventory(@PathVariable Long inventoryId, @PathVariable Integer quantity) {
        inventoryService.reduceInventory(inventoryId, quantity);
        return ResponseEntity.ok("Stock reduced successfully");
    }

    // AUMENTAR STOCK
    @PatchMapping("/{inventoryId}/increase/{quantity}")
    public ResponseEntity<String> increaseInventory(@PathVariable Long inventoryId, @PathVariable Integer quantity) {
        inventoryService.increaseInventory(inventoryId, quantity);
        return ResponseEntity.ok("Stock increased successfully");
    }

    // ELIMINAR INVENTARIO
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.ok("Inventory deleted successfully");
    }
}
