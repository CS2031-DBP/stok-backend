package com.example.stokapp.supplier.infrastructure;

import com.example.stokapp.supplier.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
