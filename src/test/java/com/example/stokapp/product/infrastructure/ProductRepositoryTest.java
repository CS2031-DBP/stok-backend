package com.example.stokapp.product.infrastructure;

import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setName("Widget");
        product.setDescription("A useful widget");
        product.setPrice(19.99);
        product.setCategory(Category.Chocolate);
        entityManager.persistAndFlush(product);
    }

    @Test
    public void whenFindByName_thenReturnProduct() {
        Optional<Product> found = productRepository.findByName(product.getName());

        assertThat(found).isNotEmpty();
        assertThat(found.get().getName()).isEqualTo(product.getName());
        assertThat(found.get().getDescription()).isEqualTo(product.getDescription());
        assertThat(found.get().getPrice()).isEqualTo(product.getPrice());
        assertThat(found.get().getCategory()).isEqualTo(product.getCategory());
    }
}

