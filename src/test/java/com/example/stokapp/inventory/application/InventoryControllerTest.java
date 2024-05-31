package com.example.stokapp.inventory.application;

import com.example.stokapp.inventory.domain.CreateInventoryRequest;
import com.example.stokapp.inventory.domain.InventoryDto;
import com.example.stokapp.inventory.domain.InventoryService;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testGetAllInventories() throws Exception {
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(99.99);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");

        InventoryDto inventory = new InventoryDto();
        inventory.setProduct(product);
        inventory.setStock(100);
        List<InventoryDto> inventoryList = Arrays.asList(inventory);
        when(inventoryService.findAll(1L)).thenReturn(inventoryList);

        mockMvc.perform(get("/inventory/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].product.name").value("Product Name"));

        verify(inventoryService, times(1)).findAll(1L);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testCreateInventory() throws Exception {
        CreateInventoryRequest request = new CreateInventoryRequest();
        request.setOwnerId(1L);
        request.setProductId(1L);
        request.setQuantity(100);

        mockMvc.perform(post("/inventory/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerId\": 1, \"productId\": 1, \"quantity\": 100}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Inventory created successfully"));

        verify(inventoryService, times(1)).createInventory(1L, 1L, 100);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testFindProductByName() throws Exception {
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(99.99);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");

        InventoryDto inventory = new InventoryDto();
        inventory.setProduct(product);
        inventory.setStock(100);
        when(inventoryService.getInventoryByProductName(1L, "Product Name")).thenReturn(inventory);

        mockMvc.perform(get("/inventory/find/1")
                        .param("productName", "Product Name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.name").value("Product Name"));

        verify(inventoryService, times(1)).getInventoryByProductName(1L, "Product Name");
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testReduceInventory() throws Exception {
        doNothing().when(inventoryService).reduceInventory(1L, 1L, 10);

        mockMvc.perform(patch("/inventory/1/1/reduce/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reduced successfully"));

        verify(inventoryService, times(1)).reduceInventory(1L, 1L, 10);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testIncreaseInventory() throws Exception {
        doNothing().when(inventoryService).increaseInventory(1L, 1L, 10);

        mockMvc.perform(patch("/inventory/1/1/increase/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock increased successfully"));

        verify(inventoryService, times(1)).increaseInventory(1L, 1L, 10);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testDeleteInventory() throws Exception {
        doNothing().when(inventoryService).deleteInventory(1L, 1L);

        mockMvc.perform(delete("/inventory/delete/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Inventory deleted successfully"));

        verify(inventoryService, times(1)).deleteInventory(1L, 1L);
    }
}