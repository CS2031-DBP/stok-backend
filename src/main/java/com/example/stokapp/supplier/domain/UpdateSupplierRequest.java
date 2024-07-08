package com.example.stokapp.supplier.domain;

import lombok.Data;

@Data
public class UpdateSupplierRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}