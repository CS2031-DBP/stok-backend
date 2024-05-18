package com.example.stokapp.product.infrastructure;

import com.example.stokapp.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}