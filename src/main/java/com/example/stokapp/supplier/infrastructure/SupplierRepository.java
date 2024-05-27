package com.example.stokapp.supplier.infrastructure;

import com.example.stokapp.supplier.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // List<Supplier> findSuppliersByOwnerId(Long id);
}
