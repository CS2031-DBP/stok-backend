package com.example.stokapp.inventory.application;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.inventory.domain.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // GET INVENTARIO
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    // CREAR INVENTARIO
    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody Inventory inventory) {
        inventoryService.createInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory created successfully");
    }

    // GET INVENTORY BY PRODUCT NAME
    @GetMapping("/find")
    public ResponseEntity<InventoryDto> findProductByName(@RequestBody String string){
        InventoryDto inventory = inventoryService.getInventoryByProductName(string);
        return ResponseEntity.ok(inventory);
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
