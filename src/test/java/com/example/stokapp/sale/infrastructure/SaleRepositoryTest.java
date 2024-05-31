package com.example.stokapp.sale.infrastructure;

import com.example.stokapp.inventory.domain.Inventory;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.sale.domain.Sale;
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

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class SaleRepositoryTest {

    Sale sale;
    Product product;
    Owner owner;
    Inventory inventory;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setName("Doritos");
        product.setDescription("Con extra queso");
        product.setPrice(2.50);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");
        entityManager.persist(product);

        owner = new Owner();
        owner.setEmail("example@example.com");
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setRole(Role.OWNER);
        owner.setPhoneNumber("1234567890");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("password");
        entityManager.persist(owner);

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStock(10);
        inventory.setOwner(owner);
        entityManager.persist(inventory);

        sale = new Sale();
        sale.setAmount(5);
        sale.setSaleCant(12.50);
        sale.setCreatedAt(ZonedDateTime.now());
        sale.setOwner(owner);
        sale.setInventory(inventory);
        entityManager.persist(sale);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnSale() {
        saleRepository.save(sale);
        ownerRepository.save(owner);
        productRepository.save(product);

        Sale found = saleRepository.findById(sale.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(sale.getId());
        assertThat(found.getAmount()).isEqualTo(sale.getAmount());
        assertThat(found.getSaleCant()).isEqualTo(sale.getSaleCant());
        assertThat(found.getCreatedAt()).isEqualTo(sale.getCreatedAt());
        assertThat(found.getOwner()).isEqualTo(sale.getOwner());
        assertThat(found.getInventory()).isEqualTo(sale.getInventory());
    }
}
