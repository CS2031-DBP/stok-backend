package com.example.stokapp.product.application;

import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.domain.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // FIND ALL PRODUCTS
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/findall")
    public ResponseEntity<List<Product>> findAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    // ADD PRODUCT
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added");
    }

    // DELETE PRODUCT
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted");
    }

    // UPDATE PRODUCT
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok("Product updated");
    }
}
