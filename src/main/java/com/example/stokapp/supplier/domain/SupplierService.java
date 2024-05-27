package com.example.stokapp.supplier.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private AuthImpl authImpl;

    // ADD SUPPLIER
    public void addSupplier(Long ownerId, Supplier supplier) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        ownerService.AddSupplier(ownerId, supplier);
        supplierRepository.save(supplier);
    }

    // DELETE SUPPLIER
    public void deleteSupplier(Long ownerId, Long supplierId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        ownerService.DeleteSupplier(ownerId, supplier);
        supplierRepository.delete(supplier);
    }

    // UPDATE SUPPLIER
    public void updateSupplier(Long ownerId, Long supplierId, Supplier updatedSupplier) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setFirstName(updatedSupplier.getFirstName());
        existingSupplier.setLastName(updatedSupplier.getLastName());
        existingSupplier.setEmail(updatedSupplier.getEmail());
        existingSupplier.setPhoneNumber(updatedSupplier.getPhoneNumber());

        supplierRepository.save(existingSupplier);
    }

    // FIND ALL SUPPLIERS
    public List<Supplier> findAllSuppliers(Long ownerId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");
        return supplierRepository.findSuppliersByOwnerId(ownerId);
    }
}