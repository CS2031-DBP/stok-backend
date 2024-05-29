package com.example.stokapp.supplier.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ProductRepository productRepository;

    public void addSupplier(CreateSupplierRequest supplierRequest) {
        Long ownerId = supplierRequest.getOwnerId();
        String firstName = supplierRequest.getFirstName();
        String lastName = supplierRequest.getLastName();
        String email = supplierRequest.getEmail();
        String phoneNumber = supplierRequest.getPhoneNumber();

        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));

        Supplier supplier = new Supplier();
        supplier.setFirstName(firstName);
        supplier.setLastName(lastName);
        supplier.setEmail(email);
        supplier.setPhoneNumber(phoneNumber);
        supplier.setOwner(owner);

        // Primero guarda el proveedor en la base de datos
        supplierRepository.save(supplier);

        // Luego añade el proveedor al propietario
        owner.getSuppliers().add(supplier);
        ownerRepository.save(owner);
    }

    // DELETE SUPPLIER
    public void deleteSupplier(Long ownerId, Long supplierId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // Eliminar la relación entre el proveedor y los productos
        supplier.getProducts().forEach(product -> product.setSupplier(null));

        // Eliminar la relación entre el propietario y el proveedor
        Owner owner = supplier.getOwner();
        owner.getSuppliers().remove(supplier);
        ownerRepository.save(owner);

        // Eliminar el proveedor
        supplierRepository.delete(supplier);
    }

    // UPDATE SUPPLIER
    public void updateSupplier(UpdateSupplierRequest updateRequest) {
        Long ownerId = updateRequest.getOwnerId();
        Long supplierId = updateRequest.getSupplierId();

        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setFirstName(updateRequest.getFirstName());
        existingSupplier.setLastName(updateRequest.getLastName());
        existingSupplier.setEmail(updateRequest.getEmail());
        existingSupplier.setPhoneNumber(updateRequest.getPhoneNumber());

        supplierRepository.save(existingSupplier);
    }

    public void addProductToSupplier(Long ownerId, Long supplierId, Long productId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        supplier.getProducts().add(product);
        product.setSupplier(supplier);

        supplierRepository.save(supplier);
        productRepository.save(product);
    }

    public void removeProductFromSupplier(Long ownerId, Long supplierId, Long productId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        supplier.getProducts().remove(product);
        product.setSupplier(null);

        supplierRepository.save(supplier);
        productRepository.save(product);
    }

    // FIND ALL SUPPLIERS
    public List<SupplierDto> findAllSuppliers(Long ownerId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        List<Supplier> suppliers = supplierRepository.findSuppliersByOwnerId(ownerId);
        return suppliers.stream()
                .map(supplier -> {
                    SupplierDto supplierDto = mapper.map(supplier, SupplierDto.class);
                    OwnerResponseDto ownerDto = mapper.map(supplier.getOwner(), OwnerResponseDto.class);
                    supplierDto.setOwnerResponseDto(ownerDto);
                    return supplierDto;
                })
                .collect(Collectors.toList());
    }
}
