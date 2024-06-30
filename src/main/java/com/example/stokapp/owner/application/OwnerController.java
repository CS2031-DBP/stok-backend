package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.*;
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


    // GET PROPIETARIO
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> findOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<OwnerResponseDto> getDriver() {
        return ResponseEntity.ok(ownerService.getOwnerOwnInfo());
    }

    // ELIMINAR PROPIETARIO
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner deleted");
    }

    // ACTUALIZAR PROPIETARIO
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PatchMapping("/update/{ownerId}")
    public ResponseEntity<String> updateOwner(@PathVariable Long ownerId, @RequestBody UpdateOwnerInfo updateOwnerInfo) {
        ownerService.updateOwner(ownerId, updateOwnerInfo);
        return ResponseEntity.ok("Owner updated");
    }

    // SEND EMAIL
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/sendmail")
    public ResponseEntity<String> sendEmail(@RequestBody OwnerEmailRequest ownerEmailRequest) {
        ownerService.sendEmail(ownerEmailRequest.getOwnerId(), ownerEmailRequest.getProductId());
        return ResponseEntity.ok("Email sent");
    }
}