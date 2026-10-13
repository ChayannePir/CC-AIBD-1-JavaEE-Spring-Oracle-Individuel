package com.medical.service;

import com.medical.dto.ConsultationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface de service pour la gestion des consultations.
 * Gère les opérations métier sur les consultations entre patients et médecins.
 * Inclut la gestion des prescriptions de médicaments et des documents.
 */
public interface ConsultationService {

    /**
     * Récupère toutes les consultations avec pagination.
     */
    Page<ConsultationDTO> getAllConsultations(Pageable pageable);

    /**
     * Récupère une consultation par son ID.
     */
    ConsultationDTO getConsultationById(String id);

    /**
     * Récupère toutes les consultations d'un patient spécifique.
     *
     * @param patientId l'ID du patient
     * @param pageable paramètres de pagination
     * @return une page de consultations du patient
     */
    Page<ConsultationDTO> getConsultationsByPatient(String patientId, Pageable pageable);

    /**
     * Crée une nouvelle consultation (programmée pour l'avenir).
     *
     * @param consultationDTO données de la consultation
     * @return la consultation créée
     */
    ConsultationDTO createConsultation(ConsultationDTO consultationDTO);

    /**
     * Met à jour une consultation programmée (avant la date de consultation).
     *
     * @param id l'ID de la consultation
     * @param consultationDTO nouvelles données
     * @return la consultation mise à jour
     */
    ConsultationDTO updateConsultation(String id, ConsultationDTO consultationDTO);

    /**
     * Supprime une consultation programmée (avant la date de consultation).
     *
     * @param id l'ID de la consultation
     */
    void deleteConsultation(String id);

    /**
     * Valide une consultation programmée (marque comme effectuée).
     *
     * @param id l'ID de la consultation
     * @return la consultation validée
     */
    ConsultationDTO validateConsultation(String id);

    /**
     * Ajoute une prescription de médicament à une consultation.
     *
     * @param consultationId l'ID de la consultation
     * @param medicamentId l'ID du médicament
     * @param nbPrises nombre de prises du médicament
     * @return la consultation mise à jour
     */
    ConsultationDTO addMedicamentPrescription(String consultationId, String medicamentId, Integer nbPrises);

    /**
     * Supprime une prescription de médicament d'une consultation.
     *
     * @param consultationId l'ID de la consultation
     * @param medicamentId l'ID du médicament à supprimer
     * @return la consultation mise à jour
     */
    ConsultationDTO removeMedicamentPrescription(String consultationId, String medicamentId);

    /**
     * Met à jour une prescription de médicament.
     *
     * @param consultationId l'ID de la consultation
     * @param medicamentId l'ID du médicament
     * @param nbPrises nouveau nombre de prises
     * @return la consultation mise à jour
     */
    ConsultationDTO updateMedicamentPrescription(String consultationId, String medicamentId, Integer nbPrises);

    /**
     * Attache un document à une consultation.
     *
     * @param consultationId l'ID de la consultation
     * @param file le fichier à attacher
     * @return la consultation avec le document attaché
     */
    ConsultationDTO attachDocument(String consultationId, MultipartFile file);

    /**
     * Récupère les médicaments prescrits dans une consultation.
     *
     * @param consultationId l'ID de la consultation
     * @return la consultation complète avec ses prescriptions
     */
    ConsultationDTO getConsultationMedicaments(String consultationId);
}

