package com.example.stokapp.supplier.domain;

import lombok.Data;

@Data
public class SupplierWithNoProductDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
