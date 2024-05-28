package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerInfo;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        return ResponseEntity.ok(ownerService.createOwner(owner));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @authImpl.isOwnerResource(#id)")
    public ResponseEntity<OwnerResponseDto> findOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @DeleteMapping("/delete/{ownerId}")
    @PreAuthorize("hasRole('OWNER') or @authImpl.isOwnerResource(#ownerId)")
    public ResponseEntity<String> deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner deleted");
    }

    @PatchMapping("/update/{ownerId}")
    @PreAuthorize("hasRole('OWNER') or @authImpl.isOwnerResource(#ownerId)")
    public ResponseEntity<String> updateOwner(@PathVariable Long ownerId, @RequestBody OwnerInfo ownerInfo) {
        ownerService.updateOwner(ownerId, ownerInfo);
        return ResponseEntity.ok("Owner updated");
    }

    @PostMapping("/send-email/{productId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> sendEmail(@PathVariable Long productId) {
        ownerService.sendEmail(productId);
        return ResponseEntity.ok("Email sent");
    }
}
