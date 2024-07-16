package com.example.stokapp.owner.application;

import com.example.stokapp.employee.domain.EmployeeDto;
import com.example.stokapp.owner.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // GET ALL EMPLOYEES
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/viewAllEmployees/{ownerId}")
    public ResponseEntity<List<EmployeeDto>> viewAllEmployees(@PathVariable Long ownerId) {
        return ResponseEntity.ok(ownerService.viewAllEmployees(ownerId));
    }

    // Endpoint con paginación
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/viewAllEmployeesPag/{ownerId}")
    public ResponseEntity<Page<EmployeeDto>> viewAllEmployeesPag(
            @PathVariable Long ownerId,
            @RequestParam int page,
            @RequestParam int size) {

        Page<EmployeeDto> response = ownerService.viewAllEmployeesPag(ownerId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<OwnerResponseDto> getOwner() {
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
        ownerService.sendEmail(ownerEmailRequest.getOwnerId(), ownerEmailRequest.getProductId(), ownerEmailRequest.getMessage());
        return ResponseEntity.ok("Email sent");
    }
}