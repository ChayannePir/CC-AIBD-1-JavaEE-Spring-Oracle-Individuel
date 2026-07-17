package com.medical.repository;

import com.medical.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Patient.
 * Fournit les méthodes de gestion des patients dans la base de données MongoDB.
 *
 * Hérite de MongoRepository qui fournit:
 * - Opérations CRUD de base (save, delete, findAll, findById)
 * - Support de la pagination et du tri
 *
 * Méthodes personnalisées:
 * - findByNumeroSS: Recherche un patient par son numéro de sécurité sociale
 * - findByNomPATContainingIgnoreCase: Recherche les patients dont le nom contient (insensible à la casse)
 * - findByNumeroSSContainingIgnoreCaseOrNomPATContainingIgnoreCase: Recherche combinée
 */
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    /**
     * Recherche un patient par son numéro de sécurité sociale
     *
     * @param numeroSS le numéro de sécurité sociale
     * @return un Optional contenant le patient s'il existe
     */
    Optional<Patient> findByNumeroSS(String numeroSS);

    /**
     * Recherche tous les patients dont le nom commence ou contient (insensible à la casse)
     *
     * @param nomPAT le nom à rechercher
     * @param pageable pagination
     * @return une page de patients trouvés
     */
    Page<Patient> findByNomPATContainingIgnoreCase(String nomPAT, Pageable pageable);

    /**
     * Recherche les patients par numéro SS ou par nom (recherche combinée)
     * Insensible à la casse
     *
     * @param numeroSS numéro de sécurité sociale
     * @param nomPAT nom du patient
     * @param pageable pagination
     * @return une page de patients trouvés
     */
    Page<Patient> findByNumeroSSContainingIgnoreCaseOrNomPATContainingIgnoreCase(
            String numeroSS, String nomPAT, Pageable pageable);

    /**
     * Recherche tous les patients avec pagination
     *
     * @param pageable pagination
     * @return une page de patients
     */
    Page<Patient> findAll(Pageable pageable);
}

