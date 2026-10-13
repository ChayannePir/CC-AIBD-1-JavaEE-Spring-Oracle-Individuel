package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO pour l'entité Medecin.
 * Utilisé pour les réponses API concernant les médecins.
 *
 * Attributs:
 * - id: Identifiant du médecin
 * - matricule: Matricule professionnel du médecin
 * - nomMED: Nom du médecin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Données d'un médecin")
public class MedecinDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du médecin
     */
    @Schema(description = "Identifiant unique du médecin", example = "507f1f77bcf86cd799439011")
    private String id;

    /**
     * Matricule professionnel du médecin
     */
    @Schema(description = "Matricule du médecin", example = "MED001")
    private String matricule;

    /**
     * Nom complet du médecin
     */
    @Schema(description = "Nom complet du médecin", example = "Dr. Martin Durand")
    private String nomMED;
}

