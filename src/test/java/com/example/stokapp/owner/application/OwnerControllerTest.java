package com.example.stokapp.owner.application;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.supplier.infrastructure.SupplierRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(value = "joel.miguel@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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

        // Registrar un nuevo propietario
        String jsonContent = Reader.readJsonFile("/owner/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        // Obtener el token del usuario registrado para usarlo en las solicitudes futuras
        String token = jsonObject.getString("token");
        System.out.println("Token: " + token);

    }

    @Test
    public void testAuthorizedAccessToGetOwnerById() throws Exception {
        // Obtener el ID del propietario recién registrado
        Long authorizedOwnerId = ownerRepository
                .findByEmail("joel.miguel@utec.edu.pe")
                .orElseThrow()
                .getId();

        mockMvc.perform(get("/owner/{id}", authorizedOwnerId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        Long ownerIdToDelete = ownerRepository
                .findByEmail("joel.miguel@utec.edu.pe")
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
                .findByEmail("joel.miguel@utec.edu.pe")
                .orElseThrow()
                .getId();

        String updateContent = Reader.readJsonFile("/owner/update.json");

        mockMvc.perform(patch("/owner/update/{ownerId}", ownerIdToUpdate)
                        .contentType(APPLICATION_JSON)
                        .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Owner updated"));
    }
}