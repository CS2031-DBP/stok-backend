package com.example.stokapp.supplier.domain;

import com.example.stokapp.owner.domain.OwnerResponseDto;
import com.example.stokapp.product.domain.ProductDto;
import lombok.Data;
import java.util.List;

@Data
public class SupplierDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private OwnerResponseDto ownerResponseDto;

    private List<ProductDto> products;
}
