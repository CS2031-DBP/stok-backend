package com.example.stokapp.product.domain;

import com.example.stokapp.configuration.DtoConfig;
import com.example.stokapp.exceptions.NotFound;
import com.example.stokapp.product.infrastructure.ProductRepository;
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

    //ADD PRODUCT
    public void addProduct(Product product) { productRepository.save(product); }

    //DELETE PRODUCT
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFound("Product not found"));
        productRepository.delete(product);
    }

    //UPDATE PRODUCT
    public void updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        productRepository.save(existingProduct);
    }

    //BUSCAR PRODUCTO POR nombre
    public ProductDto getProductByName(String nombre) {
        Product products = productRepository.findByName(nombre)
                .orElseThrow(() -> new NotFound("Product not found"));
        ProductDto productDto = mapper.map(products, ProductDto.class);

        return productDto;
    }


    // BUSCAR TODOS LOS PRODUCTOS
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new RuntimeException("No products found.");
        }
        return products;
    }

}