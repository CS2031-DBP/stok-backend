package com.example.stokapp.owner.infrastructure;

import com.example.stokapp.owner.domain.Owner;
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
public class OwnerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OwnerRepository ownerRepository;

    private Owner owner;

    private void setUpOwner() {
        owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
        entityManager.persistAndFlush(owner);
    }

    @BeforeEach
    public void setUp() {
        setUpOwner();
    }

    @Test
    public void whenFindById_thenReturnOwner() {
        Owner found = ownerRepository.findById(owner.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(owner.getEmail());
        assertThat(found.getFirstName()).isEqualTo(owner.getFirstName());
        assertThat(found.getLastName()).isEqualTo(owner.getLastName());
        assertThat(found.getPhoneNumber()).isEqualTo(owner.getPhoneNumber());
        assertThat(found.getPassword()).isEqualTo(owner.getPassword());
        assertThat(found.getRole()).isEqualTo(owner.getRole());
    }

}
