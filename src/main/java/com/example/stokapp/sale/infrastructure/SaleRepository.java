package com.example.stokapp.sale.infrastructure;

import com.example.stokapp.sale.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findAllByOwnerId(Long ownerId, Pageable pageable);
}
