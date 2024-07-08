package com.example.stokapp.supplier.application;

import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.product.application.ProductController;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.supplier.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void testAddSupplier() throws Exception {
        Owner owner = new Owner();
        CreateSupplierRequest request = new CreateSupplierRequest();
        request.setOwnerId(owner.getId());
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("123456789");
        doNothing().when(supplierService).addSupplier(any(CreateSupplierRequest.class));

        mockMvc.perform(post("/suppliers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"123456789\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Supplier created successfully"));

        verify(supplierService, times(1)).addSupplier(request);
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void testGetAllSuppliers() throws Exception {
        OwnerResponseDto ownerResponseDto = new OwnerResponseDto();
        List<ProductDto> productDtos = new ArrayList<>();
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(1L);
        supplierDto.setFirstName("John");
        supplierDto.setLastName("Doe");
        supplierDto.setEmail("john.doe@example.com");
        supplierDto.setPhoneNumber("123456789");
        supplierDto.setOwnerResponseDto(ownerResponseDto);
        supplierDto.setProducts(productDtos);

        List<SupplierDto> suppliers = Collections.singletonList(supplierDto);
        when(supplierService.findAllSuppliers(anyLong())).thenReturn(suppliers);

        mockMvc.perform(get("/suppliers/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
        verify(supplierService, times(1)).findAllSuppliers(1L);
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void testAddProductToSupplier() throws Exception {
        doNothing().when(supplierService).addProductToSupplier(anyLong(), anyLong(), anyLong());

        mockMvc.perform(post("/suppliers/1/1/addProduct/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to supplier successfully"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void testRemoveProductFromSupplier() throws Exception {
        doNothing().when(supplierService).removeProductFromSupplier(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/suppliers/1/1/removeProduct/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from supplier successfully"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void testDeleteSupplier() throws Exception {
        doNothing().when(supplierService).deleteSupplier(anyLong(), anyLong());

        mockMvc.perform(delete("/suppliers/delete/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Supplier deleted successfully"));
    }
}