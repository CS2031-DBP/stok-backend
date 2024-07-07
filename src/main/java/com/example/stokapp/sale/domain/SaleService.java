package com.example.stokapp.sale.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.inventory.domain.InventoryforSaleDto;
import com.example.stokapp.inventory.infrastructure.InventoryRepository;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
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
    private ModelMapper mapper;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthImpl authImpl;

    @Autowired
    private InventoryRepository inventoryRepository;

    private void verifyOwnerOrEmployee(Long ownerId) {
        String currentEmail = authImpl.getCurrentEmail();
        if (currentEmail == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        boolean isOwner = authImpl.isOwnerResource(ownerId);
        boolean isEmployee = owner.getEmployees().stream()
                .anyMatch(employee -> employee.getEmail().equals(currentEmail));

        if (!isOwner && !isEmployee) {
            throw new UnauthorizeOperationException("Not allowed");
        }
    }

    @Transactional
    public SaleDto createSale(CreateSaleRequest request) {
        verifyOwnerOrEmployee(request.getOwnerId());

        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getStock() < request.getAmount()) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setStock(inventory.getStock() - request.getAmount());
        inventoryRepository.save(inventory);

        Sale sale = new Sale();
        sale.setInventory(inventory);
        sale.setAmount(request.getAmount());
        sale.setCreatedAt(ZonedDateTime.now());

        Product product = inventory.getProduct();
        double saleAmount = product.getPrice() * request.getAmount();
        sale.setSaleCant(saleAmount);

        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        sale.setOwner(owner);

        owner.getSales().add(sale);
        ownerRepository.save(owner);

        SaleDto saleDto = mapper.map(sale, SaleDto.class);
        InventoryforSaleDto inventoryDto = mapper.map(inventory, InventoryforSaleDto.class);
        saleDto.setInventoryforSaleDto(inventoryDto);

        return saleDto;
    }

    @Transactional
    public void deleteSale(Long ownerId, Long saleId) {
        verifyOwnerOrEmployee(ownerId);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        inventoryService.increaseInventory(ownerId, sale.getInventory().getId(), sale.getAmount());

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.getSales().remove(sale);
        ownerRepository.save(owner);

        saleRepository.delete(sale);
    }

    @Transactional
    public void updateSale(UpdateSaleRequest request) {
        verifyOwnerOrEmployee(request.getOwnerId());

        Sale existingSale = saleRepository.findById(request.getSaleId())
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productRepository.findById(existingSale.getInventory().getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int difference = existingSale.getAmount() - request.getNewAmount();
        if (difference == 0) {
            throw new IllegalArgumentException("Sale not updated");
        } else if (difference < 0) {
            inventoryService.reduceInventory(existingSale.getOwner().getId(), existingSale.getInventory().getId(), -difference);
            existingSale.setAmount(request.getNewAmount());

            double cantidad = product.getPrice() * request.getNewAmount();
            BigDecimal bd = new BigDecimal(cantidad);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            existingSale.setSaleCant(bd.doubleValue());
        } else {
            inventoryService.increaseInventory(existingSale.getOwner().getId(), existingSale.getInventory().getId(), difference);
            existingSale.setAmount(request.getNewAmount());

            double cantidad = product.getPrice() * request.getNewAmount();
            BigDecimal bd = new BigDecimal(cantidad);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            existingSale.setSaleCant(bd.doubleValue());
        }

        saleRepository.save(existingSale);
    }

    public List<SaleDto> getAllSales(Long ownerId) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<Sale> sales = owner.getSales();
        return sales.stream()
                .map(sale -> {
                    SaleDto saleDto = mapper.map(sale, SaleDto.class);
                    InventoryforSaleDto inventoryDto = mapper.map(sale.getInventory(), InventoryforSaleDto.class);
                    saleDto.setInventoryforSaleDto(inventoryDto);
                    return saleDto;
                })
                .collect(Collectors.toList());
    }

    public SaleDto getSale(Long ownerId, Long saleId) {
        verifyOwnerOrEmployee(ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (!owner.getSales().contains(sale)) {
            throw new UnauthorizeOperationException("Sale does not belong to this owner");
        }

        SaleDto saleDto = mapper.map(sale, SaleDto.class);
        InventoryforSaleDto inventoryDto = mapper.map(sale.getInventory(), InventoryforSaleDto.class);
        saleDto.setInventoryforSaleDto(inventoryDto);

        return saleDto;
    }
}
