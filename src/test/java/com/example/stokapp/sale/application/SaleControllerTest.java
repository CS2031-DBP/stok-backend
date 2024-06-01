package com.example.stokapp.sale.application;

import com.example.stokapp.sale.domain.*;
import com.example.stokapp.inventory.domain.InventoryforSaleDto;
import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SaleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    private SaleDto saleDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(saleController).build();

        InventoryforSaleDto inventoryforSaleDto = new InventoryforSaleDto();
        inventoryforSaleDto.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product A");
        productDto.setDescription("Description A");
        productDto.setPrice(10.0);
        productDto.setCategory(Category.Fritura);
        productDto.setQr("QR123");

        inventoryforSaleDto.setProduct(productDto);

        saleDto = new SaleDto();
        saleDto.setId(1L);
        saleDto.setCreatedAt(ZonedDateTime.now());
        saleDto.setAmount(5);
        saleDto.setSaleCant(50.0);
        saleDto.setInventoryforSaleDto(inventoryforSaleDto);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testCreateSale() throws Exception {
        CreateSaleRequest request = new CreateSaleRequest();
        request.setOwnerId(1L);
        request.setInventoryId(1L);
        request.setAmount(5);

        when(saleService.createSale(any(CreateSaleRequest.class))).thenReturn(saleDto);

        mockMvc.perform(post("/sales/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerId\":1,\"inventoryId\":1,\"amount\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.amount", is(5)))
                .andExpect(jsonPath("$.saleCant", is(50.0)))
                .andExpect(jsonPath("$.inventoryforSaleDto.id", is(1)));
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testGetSale() throws Exception {
        when(saleService.getSale(anyLong(), anyLong())).thenReturn(saleDto);

        mockMvc.perform(get("/sales/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.amount", is(5)))
                .andExpect(jsonPath("$.saleCant", is(50.0)))
                .andExpect(jsonPath("$.inventoryforSaleDto.id", is(1)));
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testGetAllSales() throws Exception {
        List<SaleDto> sales = Collections.singletonList(saleDto);
        when(saleService.getAllSales(anyLong())).thenReturn(sales);

        mockMvc.perform(get("/sales/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].amount", is(5)))
                .andExpect(jsonPath("$[0].saleCant", is(50.0)))
                .andExpect(jsonPath("$[0].inventoryforSaleDto.id", is(1)));
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testUpdateSale() throws Exception {
        mockMvc.perform(patch("/sales/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerId\":1,\"saleId\":1,\"newAmount\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Sale updated successfully")));
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testDeleteSale() throws Exception {
        mockMvc.perform(delete("/sales/delete/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Sale deleted successfully")));
    }
}