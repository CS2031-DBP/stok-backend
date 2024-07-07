package com.example.stokapp.sale.application;

import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.sale.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Endpoint para crear una venta
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<SaleDto> createSale(@RequestBody CreateSaleRequest request) {
        SaleDto sale = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    // Endpoint para obtener una venta específica de un propietario
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{ownerId}/{saleId}")
    public ResponseEntity<SaleDto> getSale(@PathVariable Long ownerId, @PathVariable Long saleId) {
        SaleDto sale = saleService.getSale(ownerId, saleId);
        return ResponseEntity.ok(sale);
    }

    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{ownerId}")
    public ResponseEntity<List<SaleDto>> getAllSales(@PathVariable Long ownerId) {
        List<SaleDto> sales = saleService.getAllSales(ownerId);
        return ResponseEntity.ok(sales);
    }

    // GET CON PAGINACION

    @GetMapping("/all/{ownerId}")
    public ResponseEntity<Page<SaleDto>> getSalesByUser(@PathVariable Long ownerId, @RequestParam int page, @RequestParam int size) {
        Page<SaleDto> response = saleService.getSalsePage(ownerId, page, size);
        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar una venta
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PatchMapping("/update")
    public ResponseEntity<String> updateSale(@RequestBody UpdateSaleRequest request) {
        saleService.updateSale(request);
        return ResponseEntity.ok("Sale updated successfully");
    }

    // Endpoint para eliminar una venta
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/delete/{ownerId}/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable Long ownerId, @PathVariable Long saleId) {
        saleService.deleteSale(ownerId, saleId);
        return ResponseEntity.ok("Sale deleted successfully");
    }
}