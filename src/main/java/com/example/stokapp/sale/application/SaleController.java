package com.example.stokapp.sale.application;

import com.example.stokapp.event.AnnualSalesReportEvent;
import com.example.stokapp.event.AnnualSalesReportEventWithGraphic;
import com.example.stokapp.event.MonthlySalesReportEvent;
import com.example.stokapp.sale.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

    // Endpoint para enviar el reporte de ventas mensual
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/sendMonthlySalesReport")
    public ResponseEntity<String> sendMonthlySalesReport(@RequestBody SalesReportRequest request) {
        try {
            MonthlySalesReportEvent event = new MonthlySalesReportEvent(this, request.getOwnerId(), request.getEmail(), request.getMonth(), request.getYear());
            eventPublisher.publishEvent(event);
            return ResponseEntity.ok("Email sending initiated");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error initiating the email sending process");
        }
    }

    // Endpoint para enviar el reporte de ventas anual sin gráfico
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/sendAnnualSalesReportWithoutGraphic")
    public ResponseEntity<String> sendAnnualSalesReportWithoutGraphic(@RequestBody AnnualSalesReportRequest request) {
        try {
            AnnualSalesReportEvent event = new AnnualSalesReportEvent(request.getOwnerId(), request.getEmail(), request.getYear());
            eventPublisher.publishEvent(event);
            return ResponseEntity.ok("Annual sales report request received. The report will be sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error initiating the email sending process");
        }
    }

    // Endpoint para enviar el reporte de ventas anual con gráfico
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/sendAnnualSalesReportWithGraphic")
    public ResponseEntity<String> sendAnnualSalesReportWithGraphic(@RequestBody AnnualSalesReportRequest request) {
        try {
            AnnualSalesReportEventWithGraphic event = new AnnualSalesReportEventWithGraphic(request.getOwnerId(), request.getEmail(), request.getYear());
            eventPublisher.publishEvent(event);
            return ResponseEntity.ok("Annual sales report request received. The report with graphics will be sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error initiating the email sending process");
        }
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
        Page<SaleDto> response = saleService.getSalesPage(ownerId, page, size);
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