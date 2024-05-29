package com.example.stokapp.sale.application;

import com.example.stokapp.sale.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/create")
    public ResponseEntity<SaleDto> createSale(@RequestBody CreateSaleRequest request) {
        SaleDto sale = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    // Endpoint para obtener una venta específica de un propietario
    @GetMapping("/{ownerId}/{saleId}")
    public ResponseEntity<SaleDto> getSale(@PathVariable Long ownerId, @PathVariable Long saleId) {
        SaleDto sale = saleService.getSale(ownerId, saleId);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<List<SaleDto>> getAllSales(@PathVariable Long ownerId) {
        List<SaleDto> sales = saleService.getAllSales(ownerId);
        return ResponseEntity.ok(sales);
    }

    // Endpoint para actualizar una venta
    @PatchMapping("/update")
    public ResponseEntity<String> updateSale(@RequestBody UpdateSaleRequest request) {
        saleService.updateSale(request);
        return ResponseEntity.ok("Sale updated successfully");
    }

    // Endpoint para eliminar una venta
    @DeleteMapping("/delete/{ownerId}/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable Long ownerId, @PathVariable Long saleId) {
        saleService.deleteSale(ownerId, saleId);
        return ResponseEntity.ok("Sale deleted successfully");
    }
}