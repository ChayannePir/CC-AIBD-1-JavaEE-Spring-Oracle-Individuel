package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour l'entité Consultation.
 * Utilisé pour les réponses API concernant les consultations.
 *
 * Attributs:
 * - id: Identifiant de la consultation
 * - numero: Numéro de la consultation
 * - dateConsultation: Date et heure de la consultation
 * - patient: Informations du patient (DTO)
 * - medecin: Informations du médecin (DTO)
 * - medicaments: Liste des médicaments prescrits (avec nombre de prises)
 * - documentPath: Chemin du document attaché
 * - statut: Statut de la consultation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Données d'une consultation")
public class ConsultationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique de la consultation
     */
    @Schema(description = "Identifiant unique de la consultation", example = "507f1f77bcf86cd799439011")
    private String id;

    /**
     * Numéro de consultation (identifiant métier)
     */
    @Schema(description = "Numéro de la consultation", example = "CONS001")
    private String numero;

    /**
     * Date et heure de la consultation au format ISO 8601
     */
    @Schema(description = "Date et heure de la consultation", example = "2024-07-17T14:30:00")
    private LocalDateTime dateConsultation;

    /**
     * Informations détaillées du patient
     */
    @Schema(description = "Données du patient")
    private PatientDTO patient;

    /**
     * Informations détaillées du médecin
     */
    @Schema(description = "Données du médecin")
    private MedecinDTO medecin;

    /**
     * Liste des prescriptions de médicaments
     * Chaque prescription contient le médicament et le nombre de prises
     */
    @Schema(description = "Liste des médicaments prescrits")
    private List<PrescriptionDTO> medicaments;

    /**
     * Chemin d'accès au document attaché à la consultation
     */
    @Schema(description = "Chemin du document attaché", example = "/documents/CONS001_rapport.pdf")
    private String documentPath;

    /**
     * Statut de la consultation
     * Valeurs possibles: PROGRAMMEE, EFFECTUEE, ANNULEE
     */
    @Schema(description = "Statut de la consultation", example = "EFFECTUEE")
    private String statut;

    /**
     * DTO imbriqué pour une prescription (médicament + nombre de prises)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Prescription d'un médicament")
    public static class PrescriptionDTO implements Serializable {

        /**
         * Médicament prescrit
         */
        @Schema(description = "Données du médicament")
        private MedicamentDTO medicament;

        /**
         * Nombre de prises du médicament
         */
        @Schema(description = "Nombre de prises", example = "3")
        private Integer nbPrises;
    }
}

