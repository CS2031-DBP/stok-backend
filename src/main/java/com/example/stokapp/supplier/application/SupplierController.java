package com.example.stokapp.supplier.application;

import com.example.stokapp.supplier.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Endpoint para agregar un proveedor
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/create")
    public ResponseEntity<String> addSupplier(@RequestBody CreateSupplierRequest supplierRequest) {
        supplierService.addSupplier(supplierRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Supplier created successfully");
    }

    // Endpoint para obtener todos los proveedores
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/all/{ownerId}")
    public ResponseEntity<List<SupplierDto>> getAllSuppliers(@PathVariable Long ownerId) {
        List<SupplierDto> suppliers = supplierService.findAllSuppliers(ownerId);
        return ResponseEntity.ok(suppliers);
    }

    // Endpoint para actualizar un proveedor
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PatchMapping("/update")
    public ResponseEntity<String> updateSupplier(@RequestBody UpdateSupplierRequest updateRequest) {
        supplierService.updateSupplier(updateRequest);
        return ResponseEntity.ok("Supplier updated successfully");
    }

    // Endpoint para agregar un producto a un proveedor
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/{ownerId}/{supplierId}/addProduct/{productId}")
    public ResponseEntity<String> addProductToSupplier(@PathVariable Long ownerId, @PathVariable Long supplierId, @PathVariable Long productId) {
        supplierService.addProductToSupplier(ownerId, supplierId, productId);
        return ResponseEntity.ok("Product added to supplier successfully");
    }

    // Endpoint para quitar un producto de un proveedor
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("/{ownerId}/{supplierId}/removeProduct/{productId}")
    public ResponseEntity<String> removeProductFromSupplier(@PathVariable Long ownerId, @PathVariable Long supplierId, @PathVariable Long productId) {
        supplierService.removeProductFromSupplier(ownerId, supplierId, productId);
        return ResponseEntity.ok("Product removed from supplier successfully");
    }

    // Endpoint para eliminar un proveedor
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("/delete/{ownerId}/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long supplierId, @PathVariable Long ownerId) {
        supplierService.deleteSupplier(ownerId, supplierId);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}