package com.example.stokapp.supplier.domain;

import lombok.Data;

import java.util.List;

@Data
public class CreateSupplierRequest {
    private Long ownerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
