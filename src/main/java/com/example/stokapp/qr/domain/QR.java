package com.example.stokapp.qr.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_code", nullable = false, unique = true)
    private String qrCode;
}