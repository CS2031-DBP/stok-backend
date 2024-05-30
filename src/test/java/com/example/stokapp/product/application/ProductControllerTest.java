package com.example.stokapp.product.application;

import com.example.stokapp.product.domain.Category;
import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.domain.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testFindAllProducts() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Product Name");
        productDto.setDescription("Product Description");
        productDto.setPrice(99.99);
        productDto.setCategory(Category.Fritura);
        List<ProductDto> productList = Arrays.asList(productDto);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/product/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Product Name"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(99.99);
        product.setCategory(Category.Fritura);
        product.setQr("QR12345");

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/add")
                        .contentType("application/json")
                        .content("{\"name\": \"Product Name\", \"description\": \"Product Description\", \"price\": 99.99, \"category\": \"Fritura\", \"qr\": \"QR12345\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product Name"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/product/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted"));

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EMPLOYEE"})
    public void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product Name");
        updatedProduct.setDescription("Updated Product Description");
        updatedProduct.setPrice(199.99);
        updatedProduct.setCategory(Category.Fritura);
        updatedProduct.setQr("QR54321");

        doNothing().when(productService).updateProduct(anyLong(), any(Product.class));

        mockMvc.perform(put("/product/update/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Product Name\", \"description\": \"Updated Product Description\", \"price\": 199.99, \"category\": \"Fritura\", \"qr\": \"QR54321\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated"));

        verify(productService, times(1)).updateProduct(anyLong(), any(Product.class));
    }
}