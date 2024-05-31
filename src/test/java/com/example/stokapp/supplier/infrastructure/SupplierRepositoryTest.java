package com.example.stokapp.supplier.infrastructure;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class SupplierRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SupplierRepository supplierRepository;

    private Supplier supplier;
    private Owner owner;
    private List<Product> products;

    private void setUpSupplier() {
        owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
        entityManager.persistAndFlush(owner);

        products = new ArrayList<>();
        supplier = new Supplier();
        supplier.setFirstName("John");
        supplier.setLastName("Doe");
        supplier.setEmail("john.doe@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setOwner(owner);
        supplier.setProducts(products);

        owner.getSuppliers().add(supplier);
        entityManager.persistAndFlush(supplier);
    }

    @BeforeEach
    public void setUp() {
        setUpSupplier();
    }

    @Test
    public void whenFindById_thenReturnSupplier() {
        Supplier found = supplierRepository.findById(supplier.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(supplier.getEmail());
        assertThat(found.getFirstName()).isEqualTo(supplier.getFirstName());
        assertThat(found.getLastName()).isEqualTo(supplier.getLastName());
        assertThat(found.getPhoneNumber()).isEqualTo(supplier.getPhoneNumber());
        assertThat(found.getProducts()).isEqualTo(supplier.getProducts());
        assertThat(found.getOwner()).isEqualTo(supplier.getOwner());
    }

    @Test
    public void whenFindByOwnerId_thenReturnSupplierList() {
        List<Supplier> found = supplierRepository.findSuppliersByOwnerId(owner.getId());

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isNotNull();
        assertThat(found.get(0).getEmail()).isEqualTo(supplier.getEmail());
        assertThat(found.get(0).getFirstName()).isEqualTo(supplier.getFirstName());
        assertThat(found.get(0).getLastName()).isEqualTo(supplier.getLastName());
        assertThat(found.get(0).getPhoneNumber()).isEqualTo(supplier.getPhoneNumber());
        assertThat(found.get(0).getProducts()).isEqualTo(supplier.getProducts());
        assertThat(found.get(0).getOwner()).isEqualTo(supplier.getOwner());
    }
}
