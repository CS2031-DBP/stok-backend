package com.example.stokapp.employee.infrastructure;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
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
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setEmail("employee@example.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPassword("password123");
        employee.setRole(Role.EMPLOYEE);
        employee.setCreatedAt(ZonedDateTime.now());
        employee.setPhoneNumber("123456789");
        entityManager.persistAndFlush(employee);
    }

    @Test
    public void whenFindById_thenReturnEmployee() {
        Employee found = employeeRepository.findById(employee.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(employee.getEmail());
        assertThat(found.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(found.getLastName()).isEqualTo(employee.getLastName());
        assertThat(found.getPassword()).isEqualTo(employee.getPassword());
        assertThat(found.getRole()).isEqualTo(employee.getRole());
        assertThat(found.getPhoneNumber()).isEqualTo(employee.getPhoneNumber());
    }
}
