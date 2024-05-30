package com.example.stokapp.product.domain;

import com.example.stokapp.auth.AuthImpl;
import com.example.stokapp.configuration.DtoConfig;
import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.exceptions.UnauthorizeOperationException;
import com.example.stokapp.product.infrastructure.ProductRepository;
import com.example.stokapp.supplier.domain.SupplierWithNoProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ModelMapper mapper;
    @Autowired
    private AuthImpl authImpl;

    // ADD PRODUCT
    public ProductDto addProduct(Product product) {
        String username = authImpl.getCurrentEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    //DELETE PRODUCT
    public void deleteProduct(Long productId) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFound("Product not found"));
        productRepository.delete(product);
    }

    //UPDATE PRODUCT
    public void updateProduct(Long productId, Product updatedProduct) {
        String username = authImpl.getCurrentEmail();
        if(username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        productRepository.save(existingProduct);
    }

    // BUSCAR TODOS LOS PRODUCTOS
    public List<ProductWithSupplierDto> getAllProducts() {
        String username = authImpl.getCurrentEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Not allowed");
        }

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return products.stream()
                .map(product -> {
                    ProductWithSupplierDto productDto = mapper.map(product, ProductWithSupplierDto.class);
                    if (product.getSupplier() != null) {
                        SupplierWithNoProductDto supplierDto = mapper.map(product.getSupplier(), SupplierWithNoProductDto.class);
                        productDto.setSupplier(supplierDto);
                    }
                    return productDto;
                })
                .collect(Collectors.toList());
    }

}