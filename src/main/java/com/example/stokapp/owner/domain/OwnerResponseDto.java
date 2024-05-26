package com.example.stokapp.owner.domain;

import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OwnerResponseDto {
    @NotNull
    private Long id;
    @NotNull
    private Role role;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;

    private List<Supplier> suppliers;
    private List<Sale> sales;
}