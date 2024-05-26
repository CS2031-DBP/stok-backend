package com.example.stokapp.auth.dto;

import com.example.stokapp.user.domain.Role;
import lombok.Data;

@Data
public class AuthJwtResponse {
    public String token;
    public Long id;
    public Role role;
}