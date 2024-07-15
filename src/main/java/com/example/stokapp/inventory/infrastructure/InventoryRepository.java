package com.example.stokapp.inventory.infrastructure;

import com.example.stokapp.inventory.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Page<Inventory> findAllByOwnerId(Long id, Pageable pageable);
    Inventory findByProductId(Long productId);
}