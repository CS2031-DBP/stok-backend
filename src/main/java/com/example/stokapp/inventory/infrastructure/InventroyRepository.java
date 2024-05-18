package com.example.stokapp.inventory.infrastructure;

import com.example.stokapp.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventroyRepository extends JpaRepository<Inventory, Long> {




}
