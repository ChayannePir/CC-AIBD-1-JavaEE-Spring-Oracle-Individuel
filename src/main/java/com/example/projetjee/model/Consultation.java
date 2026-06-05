package com.example.projetjee.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate; // Using LocalDate for date without time

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "medecin_matricule", nullable = false) // Consultation must have a Medecin
    private Medecin medecin;

    @ManyToOne
    @JoinColumn(name = "patient_nss", nullable = false) // Consultation must have a Patient
    private Patient patient;
}
