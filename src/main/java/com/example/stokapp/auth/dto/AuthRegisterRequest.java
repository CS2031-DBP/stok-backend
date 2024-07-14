package com.example.stokapp.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthRegisterRequest {
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    String email;
    @NotNull
    String password;
    Boolean isOwner = false;
    @NotNull
    String phoneNumber;
}