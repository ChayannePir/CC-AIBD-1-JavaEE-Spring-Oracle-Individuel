package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO pour l'entité Patient.
 * Utilisé pour les réponses API concernant les patients.
 *
 * Attributs:
 * - id: Identifiant du patient
 * - numeroSS: Numéro de sécurité sociale
 * - nomPAT: Nom du patient
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Données d'un patient")
public class PatientDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du patient
     */
    @Schema(description = "Identifiant unique du patient", example = "507f1f77bcf86cd799439011")
    private String id;

    /**
     * Numéro de sécurité sociale du patient
     */
    @Schema(description = "Numéro de sécurité sociale", example = "1234567890123")
    private String numeroSS;

    /**
     * Nom du patient
     */
    @Schema(description = "Nom complet du patient", example = "Jean Dupont")
    private String nomPAT;
}

