package com.example.stokapp.sale.infrastructure;

import com.example.stokapp.sale.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
