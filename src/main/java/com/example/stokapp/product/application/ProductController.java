package com.example.stokapp.product.application;

import com.example.stokapp.product.domain.Product;
import com.example.stokapp.product.domain.ProductDto;
import com.example.stokapp.product.domain.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/find")
    public ResponseEntity<ProductDto> findProductByName(@RequestBody String string){
        ProductDto product= productService.getProductByName(string);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Product>> findAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    // ADD PRODUCT
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added");
    }

    // DELETE PRODUCT
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted");
    }

    // UPDATE PRODUCT
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok("Product updated");
    }
}
