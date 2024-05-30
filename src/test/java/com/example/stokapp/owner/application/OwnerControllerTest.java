package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
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
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(value = "santiago.silva@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WithMockUser(roles = "OWNER")
@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerRepository ownerRepository;

    String token = "";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    public void setUp() throws Exception {

        ownerRepository.deleteAll();

        String jsonContent = Reader.readJsonFile("/owner/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
        System.out.println("Token: " + token);

    }

    @Test
    public void testAuthorizedAccessToGetOwnerById() throws Exception {
        Long authorizedOwnerId = ownerRepository
                .findByEmail("santiago.silva@utec.edu.pe")
                .orElseThrow()
                .getId();

        mockMvc.perform(get("/owner/{id}", authorizedOwnerId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        Long ownerIdToDelete = ownerRepository
                .findByEmail("santiago.silva@utec.edu.pe")
                .orElseThrow()
                .getId();

        mockMvc.perform(delete("/owner/delete/{ownerId}", ownerIdToDelete)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Owner deleted"));

        Owner deletedOwner = ownerRepository.findById(ownerIdToDelete).orElse(null);
        assertNull(deletedOwner);
    }

    @Test
    public void testUpdateOwner() throws Exception {
        Long ownerIdToUpdate = ownerRepository
                .findByEmail("santiago.silva@utec.edu.pe")
                .orElseThrow()
                .getId();

        String updateContent = Reader.readJsonFile("/owner/update.json");

        mockMvc.perform(patch("/owner/update/{ownerId}", ownerIdToUpdate)
                        .contentType(APPLICATION_JSON)
                        .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Owner updated"));

        Owner updatedOwner = ownerRepository.findById(ownerIdToUpdate).orElseThrow();
        JSONObject updateJson = new JSONObject(updateContent);
        String newPhoneNumber = updateJson.getString("phoneNumber");

        assert(updatedOwner.getPhoneNumber().equals(newPhoneNumber));
    }

    @Test
    public void testSendEmail() throws Exception {
        Long ownerId = ownerRepository
                .findByEmail("santiago.silva@utec.edu.pe")
                .orElseThrow()
                .getId();
        Owner owner = ownerRepository.findById(ownerId).orElseThrow();

        Supplier supplier = new Supplier();
        List<Product> products = new ArrayList<>();
        supplier.setId(1L);
        supplier.setFirstName("John");
        supplier.setLastName("Doe");
        supplier.setEmail("john.doe@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setOwner(owner);
        supplier.setProducts(products);
        supplierRepository.save(supplier);

        Product product = new Product();
        product.setName("Doritos");
        product.setDescription("Con extra queso");
        product.setPrice(5.0);
        product.setCategory(Category.Fritura);
        product.setQr("Q1234");
        product.setSupplier(supplier);

        Product savedProduct = productRepository.save(product);

        Long productId = savedProduct.getId();

        String requestContent = String.format("{\"ownerId\": %d, \"productId\": %d}", ownerId, productId);

        mockMvc.perform(post("/owner/sendmail")
                        .contentType(APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent"));
    }
}