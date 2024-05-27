package com.example.stokapp.sale.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.inventory.domain.InventoryforSaleDto;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerService;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.sale.infrastructure.SaleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ModelMapper mapper;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthImpl authImpl;

    // CREAR VENTA (REDUCE STOCK DE INVENTARIO)
    @Transactional
    public void createSale(Long ownerId, Long employeeId , Sale sale) {
        if (!authImpl.isOwnerResource(ownerId) && !authImpl.isOwnerResource(employeeId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        inventoryService.reduceInventory(sale.getInventory().getId(), sale.getAmount());
        sale.setCreatedAt(ZonedDateTime.now());

        // Obtener el producto relacionado con la venta
        Product product = productRepository.findById(sale.getInventory().getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Calcular el precio total del producto
        sale.setSaleCant(product.getPrice() * sale.getAmount());

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getSales().add(sale);
        ownerRepository.save(owner);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.getSales().add(sale);
        employeeRepository.save(employee);

        saleRepository.save(sale);
    }

    // ELIMINAR VENTA (AUMENTA STOCK ELIMINADO DE INVENTARIO)
    @Transactional
    public void deleteSale(Long ownerId, Long employeeId, Long saleId) {
        if (!authImpl.isOwnerResource(ownerId) && !authImpl.isOwnerResource(employeeId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        inventoryService.increaseInventory(sale.getInventory().getId(), sale.getAmount());

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getSales().remove(sale);
        ownerRepository.save(owner);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.getSales().remove(sale);
        employeeRepository.save(employee);

        saleRepository.delete(sale);
    }

    public void updateSale(Long saleId, Sale updatedSale) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        // Obtener el producto relacionado con la venta
        Product product = productRepository.findById(existingSale.getInventory().getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int difference = existingSale.getAmount() - updatedSale.getAmount();
        if (difference == 0) {
            throw new IllegalArgumentException("Sale not updated");
        }
        else if (difference < 0) {
            inventoryService.reduceInventory(existingSale.getInventory().getId(), difference * (-1));
            existingSale.setAmount(updatedSale.getAmount());

            // Calcular y redondear la cantidad
            double cantidad = product.getPrice() * updatedSale.getAmount();
            BigDecimal bd = new BigDecimal(cantidad);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            existingSale.setSaleCant(bd.doubleValue());
        }
        if (difference > 0) {
            inventoryService.increaseInventory(existingSale.getInventory().getId(), difference);
            existingSale.setAmount(updatedSale.getAmount());

            // Calcular y redondear la cantidad
            double cantidad = product.getPrice() * updatedSale.getAmount();
            BigDecimal bd = new BigDecimal(cantidad);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            existingSale.setSaleCant(bd.doubleValue());
        }

        saleRepository.save(existingSale);
    }

    // UN GET DE TODAS LOS SALES CREADOS
    public List<SaleDto> getAllSales() {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .map(sale -> {
                    SaleDto saleDto = mapper.map(sale, SaleDto.class);
                    InventoryforSaleDto inventoryDto = mapper.map(sale.getInventory(), InventoryforSaleDto.class);
                    saleDto.setInventoryforSaleDto(inventoryDto);
                    return saleDto;
                })
                .collect(Collectors.toList());
    }
}