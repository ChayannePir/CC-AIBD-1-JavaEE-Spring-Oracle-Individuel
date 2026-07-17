package com.medical.repository;

import com.medical.entity.Medicament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Medicament.
 * Fournit les méthodes de gestion des médicaments dans la base de données MongoDB.
 *
 * Hérite de MongoRepository qui fournit:
 * - Opérations CRUD de base (save, delete, findAll, findById)
 * - Support de la pagination et du tri
 *
 * Méthodes personnalisées:
 * - findByCode: Recherche un médicament par son code
 * - findByLibelleContainingIgnoreCase: Recherche les médicaments par libellé
 */
@Repository
public interface MedicamentRepository extends MongoRepository<Medicament, String> {

    /**
     * Recherche un médicament par son code
     *
     * @param code le code du médicament
     * @return un Optional contenant le médicament s'il existe
     */
    Optional<Medicament> findByCode(String code);

    /**
     * Recherche les médicaments dont le libellé contient le texte fourni (insensible à la casse)
     *
     * @param libelle le texte à rechercher
     * @param pageable pagination
     * @return une page de médicaments trouvés
     */
    Page<Medicament> findByLibelleContainingIgnoreCase(String libelle, Pageable pageable);

    /**
     * Recherche tous les médicaments avec pagination
     *
     * @param pageable pagination
     * @return une page de médicaments
     */
    Page<Medicament> findAll(Pageable pageable);
}

