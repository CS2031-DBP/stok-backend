package com.example.stokapp.inventory.infrastructure;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.domain.SupplierService;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
import com.example.stokapp.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class InvetoryRepositoryTest {

    Inventory inventory;
    Product product;
    Owner owner;
    Supplier supplier;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        product = new Product();
        product.setName("Doritos");
        product.setDescription("Con extra queso");
        product.setPrice(2.50);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");
        product.setSupplier(supplier);
        entityManager.persist(product);

        owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
        owner.getSuppliers().add(supplier);
        entityManager.persist(owner);

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStock(10);
        inventory.setOwner(owner);
        entityManager.persist(inventory);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnSInventory() {
        inventoryRepository.save(inventory);
        ownerRepository.save(owner);
        productRepository.save(product);

        Inventory found = inventoryRepository.findById(inventory.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(inventory.getId());
        assertThat(found.getProduct()).isEqualTo(inventory.getProduct());
        assertThat(found.getStock()).isEqualTo(inventory.getStock());
        assertThat(found.getOwner()).isEqualTo(inventory.getOwner());
    }
}