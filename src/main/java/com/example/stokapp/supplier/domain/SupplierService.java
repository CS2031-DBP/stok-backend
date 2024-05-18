package com.example.stokapp.supplier.domain;

import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // ADD SUPPLIER
    public void addSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    // DELETE SUPPLIER
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplierRepository.delete(supplier);
    }

    // UPDATE SUPPLIER
    public void updateSupplier(Long supplierId, Supplier updatedSupplier) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setFirstName(updatedSupplier.getFirstName());
        existingSupplier.setLastName(updatedSupplier.getLastName());
        existingSupplier.setEmail(updatedSupplier.getEmail());
        existingSupplier.setPhoneNumber(updatedSupplier.getPhoneNumber());

        supplierRepository.save(existingSupplier);
    }


}