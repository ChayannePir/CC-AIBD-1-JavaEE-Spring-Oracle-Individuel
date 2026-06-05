package com.example.projetjee.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the Prescription entity

    @ManyToOne
    @JoinColumn(name = "medicament_code", nullable = false)
    private Medicament medicament;

    @ManyToOne
    @JoinColumn(name = "consultation_numero", nullable = false)
    private Consultation consultation;

    private Integer nbPrises; // Attribute for the association "NB prises"
}
