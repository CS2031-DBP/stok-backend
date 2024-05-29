package com.example.stokapp.sale.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.inventory.domain.InventoryforSaleDto;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
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
    @Autowired
    private InventoryRepository inventoryRepository;

    // CREAR VENTA (REDUCE STOCK DE INVENTARIO)
    @Transactional
    public void createSale(CreateSaleRequest request) {
        if (!authImpl.isOwnerResource(request.getOwnerId())) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        // Obtener el inventario de la base de datos
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        // Verificar si la cantidad solicitada está disponible en el inventario
        if (inventory.getStock() < request.getAmount()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Reducir la cantidad en el inventario
        inventory.setStock(inventory.getStock() - request.getAmount());
        inventoryRepository.save(inventory);

        // Crear la venta
        Sale sale = new Sale();
        sale.setInventory(inventory);
        sale.setAmount(request.getAmount());
        sale.setCreatedAt(ZonedDateTime.now());

        // Calcular el monto de la venta
        Product product = inventory.getProduct();
        double saleAmount = product.getPrice() * request.getAmount();
        sale.setSaleCant(saleAmount);

        // Obtener el propietario de la venta
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getSales().add(sale);

        // Guardar el propietario y la venta en la base de datos
        ownerRepository.save(owner);
        saleRepository.save(sale);
    }

    // ELIMINAR VENTA (AUMENTA STOCK ELIMINADO DE INVENTARIO)
    @Transactional
    public void deleteSale(Long ownerId, Long saleId) {
        if (!authImpl.isOwnerResource(ownerId)) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        inventoryService.increaseInventory(sale.getInventory().getId(), sale.getAmount());

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getSales().remove(sale);
        ownerRepository.save(owner);


        saleRepository.delete(sale);
    }

    @Transactional
    public void updateSale(Long saleId, Integer newAmount) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productRepository.findById(existingSale.getInventory().getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int difference = existingSale.getAmount() - newAmount;
        if (difference == 0) {
            throw new IllegalArgumentException("Sale not updated");
        } else if (difference < 0) {
            inventoryService.reduceInventory(existingSale.getInventory().getId(), -difference);
            existingSale.setAmount(newAmount);

            double cantidad = product.getPrice() * newAmount;
            BigDecimal bd = new BigDecimal(cantidad);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            existingSale.setSaleCant(bd.doubleValue());
        } else {
            inventoryService.increaseInventory(existingSale.getInventory().getId(), difference);
            existingSale.setAmount(newAmount);

            double cantidad = product.getPrice() * newAmount;
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