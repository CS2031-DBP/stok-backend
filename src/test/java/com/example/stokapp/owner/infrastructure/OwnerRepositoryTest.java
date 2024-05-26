package com.example.stokapp.owner.infrastructure;

import com.example.stokapp.owner.domain.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Owner owner;

    @BeforeEach
    public void setUp() {
        owner = new Owner();
        owner.setFirstName("Emilio");
        owner.setLastName("Silva");
        owner.setEmail("email@email.com");
        owner.setPassword("password");
        owner.setPhoneNumber("987654321");
        entityManager.persist(owner);

    }

}