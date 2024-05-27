package com.example.stokapp.supplier.application;

import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.domain.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/create")
    public ResponseEntity<String> addSupplier(@RequestBody Supplier supplier) {
        supplierService.addSupplier(supplier);
        return ResponseEntity.status(201).body("Supplier created successfully");
    }

    // Endpoint para obtener todos los proveedores
    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // Endpoint para actualizar un proveedor
    @PutMapping("/update/{supplierId}")
    public ResponseEntity<String> updateSupplier(@PathVariable Long supplierId, @RequestBody Supplier updatedSupplier) {
        supplierService.updateSupplier(supplierId, updatedSupplier);
        return ResponseEntity.ok("Supplier updated successfully");
    }

    // Endpoint para eliminar un proveedor
    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
