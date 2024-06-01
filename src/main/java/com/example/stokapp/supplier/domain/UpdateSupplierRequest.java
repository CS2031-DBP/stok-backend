package com.example.stokapp.supplier.domain;

import lombok.Data;

@Data
public class UpdateSupplierRequest {
    private Long ownerId;
    private Long supplierId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}