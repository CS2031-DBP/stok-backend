package com.example.stokapp.supplier.application;

import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.domain.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/create/{ownerId}")
    public ResponseEntity<String> addSupplier(@RequestBody Long ownerId, @RequestBody Supplier supplier) {
        supplierService.addSupplier(ownerId, supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body("Supplier created successfully");
    }

    // Endpoint para obtener todos los proveedores
    /*
    @GetMapping("/all/{ownerId}")
    public ResponseEntity<List<Supplier>> getAllSuppliers(@PathVariable Long ownerId) {
        List<Supplier> suppliers = supplierService.findAllSuppliers(ownerId);
        return ResponseEntity.ok(suppliers);
    }
    
     */

    // Endpoint para actualizar un proveedor
    @PutMapping("/update/{ownerId}/{supplierId}")
    public ResponseEntity<String> updateSupplier(@PathVariable Long ownerId, @PathVariable Long supplierId, @RequestBody Supplier updatedSupplier) {
        supplierService.updateSupplier(ownerId, supplierId, updatedSupplier);
        return ResponseEntity.ok("Supplier updated successfully");
    }

    // Endpoint para eliminar un proveedor
    @DeleteMapping("/delete/{ownerId}/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long supplierId, @PathVariable Long ownerId) {
        supplierService.deleteSupplier(ownerId, supplierId);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}