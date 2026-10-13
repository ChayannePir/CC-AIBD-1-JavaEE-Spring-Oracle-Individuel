package com.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Entité Medicament représentant un médicament dans le système.
 * Mappée à la collection MongoDB 'medicaments'.
 *
 * Attributs:
 * - id: Identifiant unique du médicament (généré automatiquement par MongoDB)
 * - code: Code unique du médicament (ex: AMOXICILLINE)
 * - libelle: Libellé descriptif du médicament (ex: Amoxicilline 500mg)
 *
 * Les médicaments peuvent être prescrits dans une consultation (relation 0,n)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medicaments")
public class Medicament implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique du médicament (généré par MongoDB)
     */
    @Id
    private String id;

    /**
     * Code unique du médicament
     * Non null, unique
     */
    private String code;

    /**
     * Libellé descriptif du médicament
     * Non null
     */
    private String libelle;
}

