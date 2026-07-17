package com.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Entité Patient représentant un patient dans le système.
 * Mappée à la collection MongoDB 'patients'.
 *
 * Attributs:
 * - id: Identifiant unique du patient (généré automatiquement par MongoDB)
 * - numeroSS: Numéro de sécurité sociale du patient (identifiant unique)
 * - nomPAT: Nom complet du patient
 *
 * Un patient peut assister à plusieurs consultations (relation 1,n avec Consultation)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patients")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique du patient (généré par MongoDB)
     */
    @Id
    private String id;

    /**
     * Numéro de sécurité sociale du patient
     * Non null, unique
     * Format: 13 chiffres en France, mais peut varier selon les pays
     */
    private String numeroSS;

    /**
     * Nom complet du patient
     * Non null
     */
    private String nomPAT;
}

