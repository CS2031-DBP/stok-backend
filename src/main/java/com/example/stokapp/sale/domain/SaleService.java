package com.example.stokapp.sale.domain;

import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.sale.infrastructure.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductRepository productRepository;

    // CREAR VENTA (REDUCE STOCK DE INVENTARIO)
    @Transactional
    public void createSale(Sale sale) {
        inventoryService.reduceInventory(sale.getInventory().getId(), sale.getAmount());
        sale.setCreatedAt(ZonedDateTime.now());

        // Obtener el producto relacionado con la venta
        Product product = productRepository.findById(sale.getInventory().getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Calcular el precio total del producto
        sale.setProductPrice(product.getPrice() * sale.getAmount());

        saleRepository.save(sale);
    }

    // ELIMINAR VENTA (AUMENTA STOCK ELIMINADO DE INVENTARIO)
    @Transactional
    public void deleteSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        inventoryService.increaseInventory(sale.getInventory().getId(), sale.getAmount());

        saleRepository.delete(sale);
    }

    // UPDATE DE LA VENTA (falta perfeccionar esto, no funciona bien tdv)
    public void updateSale(Long saleId, Sale updatedSale) {
        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        int difference = updatedSale.getAmount() - existingSale.getAmount();
        if (difference < 0) {
            throw new RuntimeException("Sale amount cannot be reduced below 0");
        }

        existingSale.setProductName(updatedSale.getProductName());
        existingSale.setProductPrice(updatedSale.getProductPrice());
        existingSale.setAmount(updatedSale.getAmount());

        if (difference > 0) {
            inventoryService.increaseInventory(existingSale.getInventory().getId(), difference);
        }

        saleRepository.save(existingSale);
    }

    // UN GET DE TODAS LOS SALES CREADOS
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
}
