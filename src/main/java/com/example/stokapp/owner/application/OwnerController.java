package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.OwnerEmailRequest;
import com.example.stokapp.owner.domain.OwnerInfo;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;


    // GET PROPIETARIO
    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> findOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    // ELIMINAR PROPIETARIO
    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner deleted");
    }

    // ACTUALIZAR PROPIETARIO
    @PatchMapping("/update/{ownerId}")
    public ResponseEntity<String> updateOwner(@PathVariable Long ownerId, @RequestBody OwnerInfo ownerInfo) {
        ownerService.updateOwner(ownerId, ownerInfo);
        return ResponseEntity.ok("Owner updated");
    }

    // SEND EMAIL
    @PostMapping("/sendmail")
    public ResponseEntity<String> sendEmail(@RequestBody OwnerEmailRequest ownerEmailRequest) {
        ownerService.sendEmail(ownerEmailRequest.getOwnerId(),ownerEmailRequest.getProductId());
        return ResponseEntity.ok("Email sent");
    }

}
