package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO pour l'entité Medicament.
 * Utilisé pour les réponses API concernant les médicaments.
 *
 * Attributs:
 * - id: Identifiant du médicament
 * - code: Code unique du médicament
 * - libelle: Libellé du médicament
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Données d'un médicament")
public class MedicamentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du médicament
     */
    @Schema(description = "Identifiant unique du médicament", example = "507f1f77bcf86cd799439011")
    private String id;

    /**
     * Code unique du médicament
     */
    @Schema(description = "Code du médicament", example = "AMOXICILLINE")
    private String code;

    /**
     * Libellé descriptif du médicament
     */
    @Schema(description = "Libellé du médicament", example = "Amoxicilline 500mg comprimé")
    private String libelle;
}

