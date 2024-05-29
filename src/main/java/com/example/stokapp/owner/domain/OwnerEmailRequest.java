package com.example.stokapp.owner.domain;

import lombok.Data;

@Data
public class OwnerEmailRequest {
    private Long ownerId;
    private Long productId;
}
