package com.medical.repository;

import com.medical.entity.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Medecin.
 * Fournit les méthodes de gestion des médecins dans la base de données MongoDB.
 *
 * Hérite de MongoRepository qui fournit:
 * - Opérations CRUD de base (save, delete, findAll, findById)
 * - Support de la pagination et du tri
 *
 * Méthodes personnalisées:
 * - findByMatricule: Recherche un médecin par son matricule
 * - findByNomMEDContainingIgnoreCase: Recherche les médecins dont le nom contient
 * - findByMatriculeContainingIgnoreCaseOrNomMEDContainingIgnoreCase: Recherche combinée
 */
@Repository
public interface MedecinRepository extends MongoRepository<Medecin, String> {

    /**
     * Recherche un médecin par son matricule
     *
     * @param matricule le matricule du médecin
     * @return un Optional contenant le médecin s'il existe
     */
    Optional<Medecin> findByMatricule(String matricule);

    /**
     * Recherche tous les médecins dont le nom commence ou contient (insensible à la casse)
     *
     * @param nomMED le nom à rechercher
     * @param pageable pagination
     * @return une page de médecins trouvés
     */
    Page<Medecin> findByNomMEDContainingIgnoreCase(String nomMED, Pageable pageable);

    /**
     * Recherche les médecins par matricule ou par nom (recherche combinée)
     * Insensible à la casse
     *
     * @param matricule matricule du médecin
     * @param nomMED nom du médecin
     * @param pageable pagination
     * @return une page de médecins trouvés
     */
    Page<Medecin> findByMatriculeContainingIgnoreCaseOrNomMEDContainingIgnoreCase(
            String matricule, String nomMED, Pageable pageable);

    /**
     * Recherche tous les médecins avec pagination
     *
     * @param pageable pagination
     * @return une page de médecins
     */
    Page<Medecin> findAll(Pageable pageable);
}

