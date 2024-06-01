package com.example.stokapp.owner.domain;

import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.supplier.domain.Supplier;
import com.example.stokapp.user.domain.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OwnerInfo {
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
}
