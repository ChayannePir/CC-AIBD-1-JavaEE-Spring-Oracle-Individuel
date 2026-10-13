package com.medical.service.impl;

import com.medical.dto.PatientDTO;
import com.medical.entity.Patient;
import com.medical.exception.ResourceAlreadyExistsException;
import com.medical.exception.ResourceNotFoundException;
import com.medical.repository.PatientRepository;
import com.medical.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation de l'interface PatientService.
 * Fournit la logique métier pour la gestion des patients.
 *
 * Utilise:
 * - PatientRepository pour accéder à la base de données
 * - Logging avec Slf4j
 * - Transactions avec @Transactional pour la cohérence des données
 * - Conversion DTO <-> Entité
 *
 * Annotations:
 * - @Service: Définit comme bean de service Spring
 * - @RequiredArgsConstructor: Génère le constructeur avec les dépendances imposées (Lombok)
 * - @Slf4j: Génère un logger (Lombok)
 * - @Transactional: Gère les transactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    // Dépendance injectée
    private final PatientRepository patientRepository;

    /**
     * Récupère tous les patients avec pagination.
     *
     * @param pageable paramètres de pagination
     * @return une page de PatientDTO
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        log.info("Récupération de tous les patients - Page: {}, Size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());

        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Recherche les patients par numéro SS et/ou nom.
     * Si les deux paramètres sont fournis, utilise une recherche combinée (OR).
     * Si un seul est fourni, utilise ce critère.
     *
     * @param numeroSS numéro de sécurité sociale
     * @param nomPAT nom du patient
     * @param pageable paramètres de pagination
     * @return une page de PatientDTO correspondant aux critères
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PatientDTO> searchPatients(String numeroSS, String nomPAT, Pageable pageable) {
        log.info("Recherche de patients - NumeroSS: {}, NomPAT: {}", numeroSS, nomPAT);

        // Cas 1: Les deux paramètres sont fournis - recherche combinée (OR)
        if (numeroSS != null && !numeroSS.isEmpty() && nomPAT != null && !nomPAT.isEmpty()) {
            log.debug("Recherche combinée avec numeroSS et nomPAT");
            return patientRepository.findByNumeroSSContainingIgnoreCaseOrNomPATContainingIgnoreCase(
                    numeroSS, nomPAT, pageable)
                    .map(this::convertToDTO);
        }

        // Cas 2: Seul le numéro SS est fourni
        if (numeroSS != null && !numeroSS.isEmpty()) {
            log.debug("Recherche par numeroSS");
            return patientRepository.findByNumeroSSContainingIgnoreCaseOrNomPATContainingIgnoreCase(
                    numeroSS, "", pageable)
                    .map(this::convertToDTO);
        }

        // Cas 3: Seul le nom est fourni
        if (nomPAT != null && !nomPAT.isEmpty()) {
            log.debug("Recherche par nomPAT");
            return patientRepository.findByNomPATContainingIgnoreCase(nomPAT, pageable)
                    .map(this::convertToDTO);
        }

        // Cas 4: Aucun paramètre fourni - tous les patients
        log.debug("Aucun critère de recherche - retour de tous les patients");
        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Récupère un patient par son ID.
     *
     * @param id l'identifiant du patient
     * @return le PatientDTO correspondant
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    @Override
    @Transactional(readOnly = true)
    public PatientDTO getPatientById(String id) {
        log.info("Récupération du patient avec l'ID: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient avec l'ID " + id + " non trouvé"));

        log.debug("Patient trouvé: {}", patient.getNomPAT());
        return convertToDTO(patient);
    }

    /**
     * Crée un nouveau patient.
     * Vérifie que le numéro SS n'existe pas déjà.
     *
     * @param patientDTO données du patient
     * @return le PatientDTO du patient créé
     * @throws ResourceAlreadyExistsException si un patient avec ce numéro SS existe
     */
    @Override
    @Transactional
    public PatientDTO createPatient(PatientDTO patientDTO) {
        log.info("Création d'un nouveau patient avec le numéro SS: {}", patientDTO.getNumeroSS());

        // Vérifier que le numéro SS n'existe pas déjà
        if (patientRepository.findByNumeroSS(patientDTO.getNumeroSS()).isPresent()) {
            log.warn("Un patient avec le numéro SS {} existe déjà", patientDTO.getNumeroSS());
            throw new ResourceAlreadyExistsException(
                    "Un patient avec le numéro SS " + patientDTO.getNumeroSS() + " existe déjà");
        }

        // Convertir DTO en entité et sauvegarder
        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient créé avec succès - ID: {}", savedPatient.getId());
        return convertToDTO(savedPatient);
    }

    /**
     * Met à jour un patient existant.
     *
     * @param id l'identifiant du patient
     * @param patientDTO nouvelles données
     * @return le PatientDTO du patient mis à jour
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    @Override
    @Transactional
    public PatientDTO updatePatient(String id, PatientDTO patientDTO) {
        log.info("Mise à jour du patient avec l'ID: {}", id);

        // Vérifier que le patient existe
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient avec l'ID " + id + " non trouvé"));

        // Mettre à jour les champs
        patient.setNumeroSS(patientDTO.getNumeroSS());
        patient.setNomPAT(patientDTO.getNomPAT());

        Patient updatedPatient = patientRepository.save(patient);

        log.info("Patient mis à jour avec succès - ID: {}", id);
        return convertToDTO(updatedPatient);
    }

    /**
     * Supprime un patient.
     *
     * @param id l'identifiant du patient
     * @throws ResourceNotFoundException si le patient n'existe pas
     */
    @Override
    @Transactional
    public void deletePatient(String id) {
        log.info("Suppression du patient avec l'ID: {}", id);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un patient inexistant - ID: {}", id);
            throw new ResourceNotFoundException(
                    "Patient avec l'ID " + id + " non trouvé");
        }

        patientRepository.deleteById(id);
        log.info("Patient supprimé avec succès - ID: {}", id);
    }

    /**
     * Convertit une entité Patient en DTO PatientDTO.
     * Utilisé pour les réponses API.
     *
     * @param patient l'entité Patient
     * @return le DTO correspondant
     */
    private PatientDTO convertToDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .numeroSS(patient.getNumeroSS())
                .nomPAT(patient.getNomPAT())
                .build();
    }

    /**
     * Convertit un DTO PatientDTO en entité Patient.
     * Utilisé pour créer/mettre à jour les patients.
     *
     * @param patientDTO le DTO
     * @return l'entité correspondante
     */
    private Patient convertToEntity(PatientDTO patientDTO) {
        return new Patient(
                null, // ID généré par MongoDB
                patientDTO.getNumeroSS(),
                patientDTO.getNomPAT()
        );
    }
}

