package com.example.stokapp.owner.domain;

import com.example.stokapp.owner.infrastructure.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    // SAVE OWNER
    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    // ELIMINAR OWNER
    public void deleteOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        ownerRepository.delete(owner);
    }

    // UPDATE OWNER
    public void updateOwner(Long ownerId, Owner updatedOwner) {
        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        existingOwner.setFirstName(updatedOwner.getFirstName());
        existingOwner.setLastName(updatedOwner.getLastName());
        existingOwner.setEmail(updatedOwner.getEmail());
        existingOwner.setPhoneNumber(updatedOwner.getPhoneNumber());
        existingOwner.setSuppliers(updatedOwner.getSuppliers());

        ownerRepository.save(existingOwner);
    }
}