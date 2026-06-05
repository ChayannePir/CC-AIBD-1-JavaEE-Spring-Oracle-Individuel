package com.example.projetjee.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-generated ID for simplicity, adjust if Code is natural key
    private Long code; // Changed to Long for auto-generation, if 'Code' is a natural key, remove @GeneratedValue
    private String libelle;

    // If 'Code' is a natural key (e.g., a specific product code), it should be String and not auto-generated.
    // Example:
    // @Id
    // private String code;
}
