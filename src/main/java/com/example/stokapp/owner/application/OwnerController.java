package com.example.stokapp.owner.application;

import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import com.example.stokapp.twilio.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private OwnerRepository ownerRepository;

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

    // ENVIAR MENSAJE DE WHATSAPP A UN PROVEEDOR
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(
            @RequestParam Long ownerId,
            @RequestParam Long supplierId,
            @RequestParam String message) {

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFound("Owner not found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFound("Supplier not found"));

        whatsAppService.sendWhatsAppMessage(owner.getPhoneNumber(), supplier.getPhoneNumber(), message);

        return ResponseEntity.ok("Message sent to supplier");
    }
}
