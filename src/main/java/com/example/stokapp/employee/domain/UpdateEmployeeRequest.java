package com.example.stokapp.employee.domain;

import lombok.Data;

@Data
public class UpdateEmployeeRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
