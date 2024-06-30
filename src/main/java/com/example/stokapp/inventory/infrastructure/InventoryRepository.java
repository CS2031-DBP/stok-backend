package com.example.stokapp.inventory.infrastructure;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByOwnerAndProduct(Owner owner, Product product);

}
