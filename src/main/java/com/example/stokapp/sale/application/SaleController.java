package com.example.stokapp.sale.application;

import com.example.stokapp.sale.domain.CreateSaleRequest;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.sale.domain.SaleDto;
import com.example.stokapp.sale.domain.SaleService;
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
    public ResponseEntity<String> createSale(@RequestBody CreateSaleRequest request) {
        saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sale created successfully");
    }

    // Endpoint para obtener todas las ventas
    @GetMapping("/all")
    public ResponseEntity<List<SaleDto>> getAllSales() {
        List<SaleDto> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    // Endpoint para actualizar una venta
    @PatchMapping("/update/{saleId}")
    public ResponseEntity<String> updateSale(@PathVariable Long saleId, @RequestBody Integer newAmount) {
        saleService.updateSale(saleId, newAmount);
        return ResponseEntity.ok("Sale updated successfully");
    }

    // Endpoint para eliminar una venta
    @DeleteMapping("/delete/{ownerId}/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable Long ownerId, @PathVariable Long saleId) {
        saleService.deleteSale(ownerId, saleId);
        return ResponseEntity.ok("Sale deleted successfully");
    }
}