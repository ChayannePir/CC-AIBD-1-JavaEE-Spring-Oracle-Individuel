package com.medical.service.impl;

import com.medical.dto.ConsultationDTO;
import com.medical.dto.MedicamentDTO;
import com.medical.dto.MedecinDTO;
import com.medical.dto.PatientDTO;
import com.medical.entity.Consultation;
import com.medical.entity.Medicament;
import com.medical.entity.Medecin;
import com.medical.entity.Patient;
import com.medical.exception.InvalidOperationException;
import com.medical.exception.ResourceNotFoundException;
import com.medical.repository.ConsultationRepository;
import com.medical.repository.MedicamentRepository;
import com.medical.repository.MedecinRepository;
import com.medical.repository.PatientRepository;
import com.medical.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implémentation de l'interface ConsultationService.
 * Fournit la logique métier pour la gestion des consultations.
 * Gère également les prescriptions de médicaments et les documents.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final MedicamentRepository medicamentRepository;

    // Répertoire pour stocker les documents
    private static final String DOCUMENTS_DIR = "documents/";

    // Statuts de consultation
    private static final String STATUT_PROGRAMMEE = "PROGRAMMEE";
    private static final String STATUT_EFFECTUEE = "EFFECTUEE";
    private static final String STATUT_ANNULEE = "ANNULEE";

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationDTO> getAllConsultations(Pageable pageable) {
        log.info("Récupération de toutes les consultations - Page: {}, Size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());

        return consultationRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationDTO getConsultationById(String id) {
        log.info("Récupération de la consultation avec l'ID: {}", id);

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + id + " non trouvée"));

        return convertToDTO(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultationDTO> getConsultationsByPatient(String patientId, Pageable pageable) {
        log.info("Récupération des consultations du patient avec l'ID: {}", patientId);

        // Vérifier que le patient existe
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient avec l'ID " + patientId + " non trouvé");
        }

        return consultationRepository.findByPatientIdOrderByDateConsultationDesc(patientId, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ConsultationDTO createConsultation(ConsultationDTO consultationDTO) {
        log.info("Création d'une nouvelle consultation pour le patient ID: {}", 
                consultationDTO.getPatient().getId());

        // Valider que la consultation est programmée pour l'avenir
        if (consultationDTO.getDateConsultation().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException(
                    "La date de consultation doit être dans le futur");
        }

        // Récupérer et valider le patient et le médecin
        Patient patient = patientRepository.findById(consultationDTO.getPatient().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient avec l'ID " + consultationDTO.getPatient().getId() + " non trouvé"));

        Medecin medecin = medecinRepository.findById(consultationDTO.getMedecin().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médecin avec l'ID " + consultationDTO.getMedecin().getId() + " non trouvé"));

        // Générer un numéro unique pour la consultation
        String numero = "CONS" + System.currentTimeMillis();

        // Créer la consultation
        Consultation consultation = Consultation.builder()
                .numero(numero)
                .dateConsultation(consultationDTO.getDateConsultation())
                .patient(patient)
                .medecin(medecin)
                .statut(STATUT_PROGRAMMEE)
                .build();

        Consultation savedConsultation = consultationRepository.save(consultation);

        log.info("Consultation créée avec succès - ID: {}, Numéro: {}", 
                savedConsultation.getId(), numero);

        return convertToDTO(savedConsultation);
    }

    @Override
    @Transactional
    public ConsultationDTO updateConsultation(String id, ConsultationDTO consultationDTO) {
        log.info("Mise à jour de la consultation avec l'ID: {}", id);

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + id + " non trouvée"));

        // Vérifier que la consultation est programmée et dans le futur
        if (!consultation.getStatut().equals(STATUT_PROGRAMMEE)) {
            throw new InvalidOperationException(
                    "Seules les consultations programmées peuvent être modifiées");
        }

        if (consultation.getDateConsultation().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException(
                    "Impossible de modifier une consultation passée");
        }

        // Valider la nouvelle date
        if (consultationDTO.getDateConsultation().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException(
                    "La nouvelle date doit être dans le futur");
        }

        // Mettre à jour les champs
        consultation.setDateConsultation(consultationDTO.getDateConsultation());

        if (consultationDTO.getPatient() != null) {
            Patient patient = patientRepository.findById(consultationDTO.getPatient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient non trouvé"));
            consultation.setPatient(patient);
        }

        if (consultationDTO.getMedecin() != null) {
            Medecin medecin = medecinRepository.findById(consultationDTO.getMedecin().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Médecin non trouvé"));
            consultation.setMedecin(medecin);
        }

        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Consultation mise à jour avec succès - ID: {}", id);
        return convertToDTO(updatedConsultation);
    }

    @Override
    @Transactional
    public void deleteConsultation(String id) {
        log.info("Suppression de la consultation avec l'ID: {}", id);

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + id + " non trouvée"));

        // Vérifier que la consultation est programmée
        if (!consultation.getStatut().equals(STATUT_PROGRAMMEE)) {
            throw new InvalidOperationException(
                    "Seules les consultations programmées peuvent être supprimées");
        }

        consultationRepository.deleteById(id);
        log.info("Consultation supprimée avec succès - ID: {}", id);
    }

    @Override
    @Transactional
    public ConsultationDTO validateConsultation(String id) {
        log.info("Validation (marque comme effectuée) de la consultation avec l'ID: {}", id);

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + id + " non trouvée"));

        // Vérifier que la consultation est programmée
        if (!consultation.getStatut().equals(STATUT_PROGRAMMEE)) {
            throw new InvalidOperationException(
                    "Seules les consultations programmées peuvent être validées");
        }

        consultation.setStatut(STATUT_EFFECTUEE);
        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Consultation validée avec succès - ID: {}", id);
        return convertToDTO(updatedConsultation);
    }

    @Override
    @Transactional
    public ConsultationDTO addMedicamentPrescription(String consultationId, String medicamentId, Integer nbPrises) {
        log.info("Ajout d'une prescription de médicament à la consultation ID: {} - Médicament ID: {}", 
                consultationId, medicamentId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + consultationId + " non trouvée"));

        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médicament avec l'ID " + medicamentId + " non trouvé"));

        // Vérifier que le médicament ne soit pas déjà prescrit
        boolean alreadyPrescribed = consultation.getMedicaments().stream()
                .anyMatch(p -> p.getMedicament().getId().equals(medicamentId));

        if (alreadyPrescribed) {
            throw new InvalidOperationException(
                    "Ce médicament est déjà prescrit pour cette consultation");
        }

        // Ajouter la prescription
        Consultation.Prescription prescription = Consultation.Prescription.builder()
                .medicament(medicament)
                .nbPrises(nbPrises)
                .build();

        consultation.getMedicaments().add(prescription);
        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Prescription ajoutée avec succès - Consultation ID: {}", consultationId);
        return convertToDTO(updatedConsultation);
    }

    @Override
    @Transactional
    public ConsultationDTO removeMedicamentPrescription(String consultationId, String medicamentId) {
        log.info("Suppression d'une prescription de la consultation ID: {} - Médicament ID: {}", 
                consultationId, medicamentId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + consultationId + " non trouvée"));

        // Vérifier que le médicament est prescrit
        boolean found = consultation.getMedicaments().removeIf(p -> 
                p.getMedicament().getId().equals(medicamentId));

        if (!found) {
            throw new ResourceNotFoundException(
                    "Le médicament n'est pas prescrit pour cette consultation");
        }

        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Prescription supprimée avec succès - Consultation ID: {}", consultationId);
        return convertToDTO(updatedConsultation);
    }

    @Override
    @Transactional
    public ConsultationDTO updateMedicamentPrescription(String consultationId, String medicamentId, Integer nbPrises) {
        log.info("Mise à jour d'une prescription pour la consultation ID: {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + consultationId + " non trouvée"));

        // Trouver et mettre à jour la prescription
        boolean found = false;
        for (Consultation.Prescription p : consultation.getMedicaments()) {
            if (p.getMedicament().getId().equals(medicamentId)) {
                p.setNbPrises(nbPrises);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException(
                    "Le médicament n'est pas prescrit pour cette consultation");
        }

        Consultation updatedConsultation = consultationRepository.save(consultation);

        log.info("Prescription mise à jour avec succès - Consultation ID: {}", consultationId);
        return convertToDTO(updatedConsultation);
    }

    @Override
    @Transactional
    public ConsultationDTO attachDocument(String consultationId, MultipartFile file) {
        log.info("Attachement d'un document à la consultation ID: {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + consultationId + " non trouvée"));

        try {
            // Créer le répertoire s'il n'existe pas
            Path documentsPath = Paths.get(DOCUMENTS_DIR);
            Files.createDirectories(documentsPath);

            // Générer un nom unique pour le fichier
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = documentsPath.resolve(filename);

            // Sauvegarder le fichier
            Files.write(filePath, file.getBytes());

            // Mettre à jour la consultation avec le chemin du document
            consultation.setDocumentPath(filePath.toString());
            Consultation updatedConsultation = consultationRepository.save(consultation);

            log.info("Document attaché avec succès - Consultation ID: {}, File: {}", 
                    consultationId, filename);

            return convertToDTO(updatedConsultation);

        } catch (IOException e) {
            log.error("Erreur lors de la sauvegarde du document", e);
            throw new InvalidOperationException(
                    "Erreur lors de la sauvegarde du document: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationDTO getConsultationMedicaments(String consultationId) {
        log.info("Récupération des médicaments prescrits pour la consultation ID: {}", consultationId);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation avec l'ID " + consultationId + " non trouvée"));

        return convertToDTO(consultation);
    }

    /**
     * Convertit une entité Consultation en DTO ConsultationDTO.
     * Effectue aussi la conversion des entités liées (Patient, Medecin, Medicaments).
     *
     * @param consultation l'entité Consultation
     * @return le DTO correspondant
     */
    private ConsultationDTO convertToDTO(Consultation consultation) {
        return ConsultationDTO.builder()
                .id(consultation.getId())
                .numero(consultation.getNumero())
                .dateConsultation(consultation.getDateConsultation())
                .patient(convertPatientToDTO(consultation.getPatient()))
                .medecin(convertMedecinToDTO(consultation.getMedecin()))
                .medicaments(consultation.getMedicaments().stream()
                        .map(p -> ConsultationDTO.PrescriptionDTO.builder()
                                .medicament(convertMedicamentToDTO(p.getMedicament()))
                                .nbPrises(p.getNbPrises())
                                .build())
                        .toList())
                .documentPath(consultation.getDocumentPath())
                .statut(consultation.getStatut())
                .build();
    }

    // Méthodes de conversion des entités liées
    private PatientDTO convertPatientToDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .numeroSS(patient.getNumeroSS())
                .nomPAT(patient.getNomPAT())
                .build();
    }

    private MedecinDTO convertMedecinToDTO(Medecin medecin) {
        return MedecinDTO.builder()
                .id(medecin.getId())
                .matricule(medecin.getMatricule())
                .nomMED(medecin.getNomMED())
                .build();
    }

    private MedicamentDTO convertMedicamentToDTO(Medicament medicament) {
        return MedicamentDTO.builder()
                .id(medicament.getId())
                .code(medicament.getCode())
                .libelle(medicament.getLibelle())
                .build();
    }
}

