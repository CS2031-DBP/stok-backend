package com.example.stokapp.employee.application;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.user.domain.Role;
import com.example.stokapp.utils.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "joel.miguel@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WithMockUser(roles = "EMPLOYEE")
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    String token = "";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    public void setUp() throws Exception {

        employeeRepository.deleteAll();

        String jsonContent = Reader.readJsonFile("/employee/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
        System.out.println("Token: " + token);

    }

    @Test
    public void testAuthorizedAccessToGetEmployeeById() throws Exception {
        Long authorizedEmployeeId = employeeRepository
                .findByEmail("joel.miguel@utec.edu.pe")
                .orElseThrow()
                .getId();

        mockMvc.perform(get("/employee/{employeeId}", authorizedEmployeeId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Long employeeIdToDelete = employeeRepository
                .findByEmail("joel.miguel@utec.edu.pe")
                .orElseThrow()
                .getId();

        Owner owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
        ownerRepository.save(owner);

        Long ownerId = owner.getId();


        mockMvc.perform(delete("/employee/delete/{ownerId}/{employeeId}", ownerId, employeeIdToDelete)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Owner deletedEmployee = ownerRepository.findById(employeeIdToDelete).orElse(null);
        assertNull(deletedEmployee);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Long employeeIdToUpdate = employeeRepository
                .findByEmail("joel.miguel@utec.edu.pe")
                .orElseThrow()
                .getId();

        String updateContent = Reader.readJsonFile("/employee/update.json");

        mockMvc.perform(patch("/employee/update/{id}", employeeIdToUpdate)
                        .contentType(APPLICATION_JSON)
                        .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated"));

        Employee updatedEmployee = employeeRepository.findById(employeeIdToUpdate).orElseThrow();
        JSONObject updateJson = new JSONObject(updateContent);
        String newPhoneNumber = updateJson.getString("phoneNumber");

        assert(updatedEmployee.getPhoneNumber().equals(newPhoneNumber));
    }

    /*
    @Test
    public void testSendEmail() throws Exception {
        // Create a product
        Product product = new Product();
        product.setName("Doritos");
        product.setDescription("Con extra queso");
        product.setPrice(5.0);
        product.setCategory(Category.Fritura);

        Product savedProduct = productRepository.save(product);

        Long productId = savedProduct.getId();

        mockMvc.perform(post("/owner/send-email/{productId}", productId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent"));
    }

     */
}
