package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    // GUARDAR PROPIETARIO
    @PostMapping("/create")
    public ResponseEntity<String> saveOwner(@RequestBody Owner owner) {
        ownerService.saveOwner(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner saved");
    }

    // ELIMINAR PROPIETARIO
    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner deleted");
    }

    // ACTUALIZAR PROPIETARIO
    @PutMapping("/update/{ownerId}")
    public ResponseEntity<String> updateOwner(@PathVariable Long ownerId, @RequestBody Owner updatedOwner) {
        ownerService.updateOwner(ownerId, updatedOwner);
        return ResponseEntity.ok("Owner updated");
    }
}
