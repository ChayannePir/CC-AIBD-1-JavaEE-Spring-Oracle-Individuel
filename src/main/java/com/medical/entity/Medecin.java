package com.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Entité Medecin représentant un médecin dans le système.
 * Mappée à la collection MongoDB 'medecins'.
 *
 * Attributs:
 * - id: Identifiant unique du médecin (généré automatiquement par MongoDB)
 * - matricule: Matricule unique du médecin (identifiant professionnel)
 * - nomMED: Nom complet du médecin
 *
 * Un médecin peut donner plusieurs consultations (relation 0,n avec Consultation)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medecins")
public class Medecin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique du médecin (généré par MongoDB)
     */
    @Id
    private String id;

    /**
     * Matricule unique du médecin (identifiant professionnel)
     * Non null, unique
     */
    private String matricule;

    /**
     * Nom complet du médecin
     * Non null
     */
    private String nomMED;
}

