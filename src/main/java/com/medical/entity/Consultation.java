package com.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Consultation représentant une consultation entre un patient et un médecin.
 * Mappée à la collection MongoDB 'consultations'.
 *
 * Attributs:
 * - id: Identifiant unique de la consultation (généré automatiquement par MongoDB)
 * - numero: Numéro de consultation (unique)
 * - dateConsultation: Date et heure de la consultation
 * - patient: Référence au patient qui assiste à la consultation
 * - medecin: Référence au médecin qui effectue la consultation
 * - medicaments: Liste des médicaments prescrits dans cette consultation avec nombre de prises
 * - documentPath: Chemin d'accès au document attaché (fichier) à la consultation
 * - statut: Statut de la consultation (PROGRAMMEE, EFFECTUEE, ANNULEE)
 *
 * Relations:
 * - 1,1 avec Patient (un patient par consultation)
 * - 1,1 avec Medecin (un médecin par consultation)
 * - 0,n avec Medicament (plusieurs médicaments prescrits via la classe Prescription)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "consultations")
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique de la consultation (généré par MongoDB)
     */
    @Id
    private String id;

    /**
     * Numéro de consultation (identifiant métier)
     * Non null, unique
     */
    private String numero;

    /**
     * Date et heure de la consultation
     * Non null
     */
    private LocalDateTime dateConsultation;

    /**
     * Référence au patient qui assiste à la consultation
     * Relation many-to-one: plusieurs consultations peut avoir un même patient
     * Non null
     */
    @DBRef
    private Patient patient;

    /**
     * Référence au médecin qui effectue la consultation
     * Relation many-to-one: plusieurs consultations peut avoir un même médecin
     * Non null
     */
    @DBRef
    private Medecin medecin;

    /**
     * Liste des médicaments prescrits dans cette consultation
     * Chaque élément contient le médicament et le nombre de prises
     * Peut être vide si aucun médicament n'est prescrit pour cette consultation
     */
    @Builder.Default
    private List<Prescription> medicaments = new ArrayList<>();

    /**
     * Chemin d'accès au document attaché à la consultation (ex: rapport, résultats)
     * Optionnel (peut être null si aucun document n'est attaché)
     */
    private String documentPath;

    /**
     * Statut de la consultation
     * Valeurs possibles: PROGRAMMEE, EFFECTUEE, ANNULEE
     * Par défaut: PROGRAMMEE
     */
    @Builder.Default
    private String statut = "PROGRAMMEE";

    /**
     * Classe imbriquée pour représenter une prescription de médicament
     * Elle établit la relation many-to-many entre Consultation et Medicament
     * avec attribut supplémentaire: nombre de prises
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Prescription implements Serializable {
        /**
         * Référence au médicament prescrit
         */
        private Medicament medicament;

        /**
         * Nombre de prises du médicament
         */
        private Integer nbPrises;
    }
}

