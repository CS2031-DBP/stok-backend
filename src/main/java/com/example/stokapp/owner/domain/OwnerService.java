package com.example.stokapp.owner.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.domain.EmployeeDto;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.event.SendEmailToSupplierEvent;
import com.example.stokapp.event.WelcomeEmailEvent;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import org.aspectj.bridge.IMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    AuthImpl authImpl;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private EmployeeRepository employeeRepository;

    // ELIMINAR OWNER
    public void deleteOwner(Long ownerId) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        ownerRepository.delete(owner);
    }

    // UPDATE OWNER
    public void updateOwner(Long ownerId, UpdateOwnerInfo updateOwnerInfo) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        existingOwner.setFirstName(updateOwnerInfo.getFirstName());
        existingOwner.setLastName(updateOwnerInfo.getLastName());
        existingOwner.setPhoneNumber(updateOwnerInfo.getPhoneNumber());

        ownerRepository.save(existingOwner);
    }

    public OwnerResponseDto getOwnerById(Long id) {
        if (!authImpl.isOwnerResource(id))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new RuntimeException("Owner not found"));

        return mapper.map(owner, OwnerResponseDto.class);
    }

    public OwnerResponseDto getOwnerOwnInfo() {
        String username = authImpl.getCurrentEmail();
        if(username == null) throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Owner owner = ownerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Owner not found"));
        return getOwnerById(owner.getId());
    }

    public void sendEmail(Long ownerId, Long productId , String message) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getSupplier() == null) {
            throw new RuntimeException("Producto no tiene un supplier asignado");
        }

        applicationEventPublisher.publishEvent(new SendEmailToSupplierEvent(this, product.getSupplier().getEmail(), product,ownerId ,message));
    }


    public List<EmployeeDto> viewAllEmployees(Long ownerId){
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<Employee> employees = owner.getEmployees();
        return employees.stream()
                .map(employee -> {
                    EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
                    return employeeDto;
                })
                .collect(Collectors.toList());
    }

    public Page<EmployeeDto> viewAllEmployeesPag(Long ownerId, int page, int size) {
        if (!authImpl.isOwnerResource(ownerId))
            throw new UnauthorizeOperationException("Not allowed");

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAllByOwner(owner, pageable);
        return employees.map(employee -> mapper.map(employee, EmployeeDto.class));
    }
}