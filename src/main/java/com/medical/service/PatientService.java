package com.medical.service;

import com.medical.dto.PatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de service pour la gestion des patients.
 * Définit les opérations métier disponibles sur les patients.
 *
 * Responsabilités:
 * - CRUD sur les patients
 * - Recherche et filtrage des patients
 * - Gestion des erreurs métier
 * - Conversion entité <-> DTO
 *
 * Implémentée par PatientServiceImpl
 */
public interface PatientService {

    /**
     * Récupère tous les patients avec pagination.
     * Opération de base sans filtres.
     *
     * @param pageable paramètres de pagination (page, size, sort)
     * @return une page contenant les patients
     */
    Page<PatientDTO> getAllPatients(Pageable pageable);

    /**
     * Recherche les patients par numéro de sécurité sociale ou nom.
     * Recherche combinée: si un paramètre est fourni, il est utilisé.
     * La recherche est insensible à la casse.
     *
     * @param numeroSS le numéro de sécurité sociale à rechercher (peut être null)
     * @param nomPAT le nom du patient à rechercher (peut être null)
     * @param pageable paramètres de pagination
     * @return une page contenant les patients correspondant aux critères
     */
    Page<PatientDTO> searchPatients(String numeroSS, String nomPAT, Pageable pageable);

    /**
     * Récupère un patient par son ID.
     *
     * @param id l'identifiant du patient
     * @return le DTO du patient
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    PatientDTO getPatientById(String id);

    /**
     * Crée un nouveau patient.
     * Vérifie que le numéro SS n'existe pas déjà.
     *
     * @param patientDTO données du patient à créer
     * @return le DTO du patient créé
     * @throws ResourceAlreadyExistsException si un patient avec ce numéro SS existe déjà
     */
    PatientDTO createPatient(PatientDTO patientDTO);

    /**
     * Met à jour un patient existant.
     * Vérifie l'existence du patient avant mise à jour.
     *
     * @param id l'identifiant du patient
     * @param patientDTO nouvelles données du patient
     * @return le DTO du patient mis à jour
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    PatientDTO updatePatient(String id, PatientDTO patientDTO);

    /**
     * Supprime un patient.
     * À cascader avec les consultations si nécessaire.
     *
     * @param id l'identifiant du patient
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    void deletePatient(String id);
}

