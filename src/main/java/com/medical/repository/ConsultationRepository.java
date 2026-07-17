package com.medical.repository;

import com.medical.entity.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Consultation.
 * Fournit les méthodes de gestion des consultations dans la base de données MongoDB.
 *
 * Hérite de MongoRepository qui fournit:
 * - Opérations CRUD de base (save, delete, findAll, findById)
 * - Support de la pagination et du tri
 *
 * Méthodes personnalisées:
 * - findByNumero: Recherche une consultation par son numéro
 * - findByPatientIdOrderByDateConsultationDesc: Recherche les consultations d'un patient
 * - findByStatut: Recherche les consultations par statut (PROGRAMMEE, EFFECTUEE, ANNULEE)
 */
@Repository
public interface ConsultationRepository extends MongoRepository<Consultation, String> {

    /**
     * Recherche une consultation par son numéro
     *
     * @param numero le numéro de la consultation
     * @return un Optional contenant la consultation si elle existe
     */
    Optional<Consultation> findByNumero(String numero);

    /**
     * Recherche toutes les consultations d'un patient, triées par date (plus récentes d'abord)
     * Utilisé pour lister les consultations d'un patient donné
     *
     * @param patientId l'identifiant du patient
     * @param pageable pagination
     * @return une page de consultations du patient, triées par date décroissante
     */
    Page<Consultation> findByPatientIdOrderByDateConsultationDesc(String patientId, Pageable pageable);

    /**
     * Recherche les consultations par statut
     *
     * @param statut le statut de la consultation (PROGRAMMEE, EFFECTUEE, ANNULEE)
     * @param pageable pagination
     * @return une page de consultations avec le statut donné
     */
    Page<Consultation> findByStatut(String statut, Pageable pageable);

    /**
     * Recherche toutes les consultations avec pagination
     *
     * @param pageable pagination
     * @return une page de consultations
     */
    Page<Consultation> findAll(Pageable pageable);
}

