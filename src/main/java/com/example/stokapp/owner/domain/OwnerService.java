package com.example.stokapp.owner.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.event.SendEmailToSupplierEvent;
import com.example.stokapp.event.WelcomeEmailEvent;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    AuthImpl authImpl;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    // ELIMINAR OWNER
    public void deleteOwner(Long ownerId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        ownerRepository.delete(owner);
    }

    // UPDATE OWNER
    public void updateOwner(Long ownerId, OwnerInfo ownerInfo) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        existingOwner.setFirstName(ownerInfo.getFirstName());
        existingOwner.setLastName(ownerInfo.getLastName());
        existingOwner.setEmail(ownerInfo.getEmail());
        existingOwner.setPhoneNumber(ownerInfo.getPhoneNumber());
        existingOwner.setRole(ownerInfo.getRole());

        ownerRepository.save(existingOwner);
    }

    public OwnerResponseDto getOwnerById(Long id) {
        if (!authImpl.isOwnerResource(id))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new RuntimeException("Owner not found"));

        return mapper.map(owner, OwnerResponseDto.class);
    }


    public void sendEmail(Long ownerId, Long productId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getSupplier() == null) {
            throw new RuntimeException("Producto no tiene un supplier asignado");
        }

        applicationEventPublisher.publishEvent(new SendEmailToSupplierEvent(this, product.getSupplier().getEmail(), product));
    }
}