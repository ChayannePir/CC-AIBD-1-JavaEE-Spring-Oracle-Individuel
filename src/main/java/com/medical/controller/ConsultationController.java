package com.medical.controller;

import com.medical.dto.ConsultationDTO;
import com.medical.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contrôleur REST pour la gestion des consultations.
 * Fournit les endpoints pour les opérations CRUD sur les consultations,
 * la gestion des prescriptions et des documents attachés.
 *
 * Endpoints principaux:
 * - GET /api/consultations - Récupère toutes les consultations
 * - GET /api/consultations/{id} - Récupère une consultation
 * - GET /api/consultations/patient/{patientId} - Récupère les consultations d'un patient
 * - POST /api/consultations - Crée une consultation programmée
 * - PUT /api/consultations/{id} - Met à jour une consultation
 * - DELETE /api/consultations/{id} - Supprime une consultation programmée
 * - PATCH /api/consultations/{id}/validate - Valide une consultation
 * - POST /api/consultations/{id}/prescriptions - Ajoute une prescription
 * - DELETE /api/consultations/{id}/prescriptions/{medicamentId} - Supprime une prescription
 * - PUT /api/consultations/{id}/prescriptions/{medicamentId} - Met à jour une prescription
 * - POST /api/consultations/{id}/documents - Attache un document
 * - GET /api/consultations/{id}/medicaments - Récupère les médicaments prescrits
 */
@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consultations", description = "Endpoints pour la gestion des consultations, prescriptions et documents")
@SecurityRequirement(name = "bearerAuth")
public class ConsultationController {

    private final ConsultationService consultationService;

    /**
     * Récupère toutes les consultations avec pagination.
     *
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de ConsultationDTO
     */
    @GetMapping
    @Operation(
            summary = "Récupère toutes les consultations",
            description = "Retourne une page paginée de toutes les consultations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consultations récupérées"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<ConsultationDTO>> getAllConsultations(
            @PageableDefault(size = 20, sort = "dateConsultation", direction = Sort.Direction.DESC)
            Pageable pageable) {

        log.info("Récupération de toutes les consultations");
        Page<ConsultationDTO> consultations = consultationService.getAllConsultations(pageable);

        return ResponseEntity.ok(consultations);
    }

    /**
     * Récupère une consultation par son ID.
     *
     * @param id l'identifiant de la consultation
     * @return ResponseEntity avec ConsultationDTO
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupère une consultation par ID",
            description = "Retourne les détails d'une consultation spécifique",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consultation trouvée"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> getConsultationById(@PathVariable String id) {
        log.info("Récupération de la consultation avec l'ID: {}", id);
        ConsultationDTO consultation = consultationService.getConsultationById(id);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Récupère toutes les consultations d'un patient donné avec pagination.
     *
     * @param patientId l'identifiant du patient
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de ConsultationDTO triées par date décroissante
     */
    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "Récupère les consultations d'un patient",
            description = "Retourne les consultations d'un patient spécifique, triées par date (plus récentes d'abord)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consultations récupérées"),
                    @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<ConsultationDTO>> getConsultationsByPatient(
            @PathVariable String patientId,
            @PageableDefault(size = 20, sort = "dateConsultation", direction = Sort.Direction.DESC)
            Pageable pageable) {

        log.info("Récupération des consultations du patient avec l'ID: {}", patientId);
        Page<ConsultationDTO> consultations = consultationService.getConsultationsByPatient(patientId, pageable);

        return ResponseEntity.ok(consultations);
    }

    /**
     * Crée une nouvelle consultation programmée (future).
     *
     * @param consultationDTO données de la consultation
     * @return ResponseEntity avec la ConsultationDTO créée
     */
    @PostMapping
    @Operation(
            summary = "Crée une nouvelle consultation",
            description = "Crée une consultation programmée pour une date future",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Consultation créée"),
                    @ApiResponse(responseCode = "400", description = "Données invalides/Date passée"),
                    @ApiResponse(responseCode = "404", description = "Patient ou médecin non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> createConsultation(@RequestBody ConsultationDTO consultationDTO) {
        log.info("Création d'une nouvelle consultation");
        ConsultationDTO createdConsultation = consultationService.createConsultation(consultationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdConsultation);
    }

    /**
     * Met à jour une consultation programmée.
     * Seules les consultations en statut PROGRAMMEE peuvent être modifiées et doivent être futures.
     *
     * @param id l'identifiant de la consultation
     * @param consultationDTO données mises à jour
     * @return ResponseEntity avec la ConsultationDTO mise à jour
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Met à jour une consultation programmée",
            description = "Met à jour une consultation qui n'a pas encore eu lieu",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consultation mise à jour"),
                    @ApiResponse(responseCode = "400", description = "Consultation non modifiable"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> updateConsultation(
            @PathVariable String id,
            @RequestBody ConsultationDTO consultationDTO) {

        log.info("Mise à jour de la consultation avec l'ID: {}", id);
        ConsultationDTO updatedConsultation = consultationService.updateConsultation(id, consultationDTO);

        return ResponseEntity.ok(updatedConsultation);
    }

    /**
     * Supprime une consultation programmée.
     * Seules les consultations en statut PROGRAMMEE peuvent être supprimées.
     *
     * @param id l'identifiant de la consultation
     * @return ResponseEntity vide avec status 204
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprime une consultation programmée",
            description = "Supprime une consultation qui n'a pas encore eu lieu",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Consultation supprimée"),
                    @ApiResponse(responseCode = "400", description = "Consultation non supprimable"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Void> deleteConsultation(@PathVariable String id) {
        log.info("Suppression de la consultation avec l'ID: {}", id);
        consultationService.deleteConsultation(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Valide une consultation (marque comme effectuée).
     * Change le statut de PROGRAMMEE à EFFECTUEE.
     *
     * @param id l'identifiant de la consultation
     * @return ResponseEntity avec la ConsultationDTO validée
     */
    @PatchMapping("/{id}/validate")
    @Operation(
            summary = "Valide une consultation",
            description = "Marque une consultation comme effectuée",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consultation validée"),
                    @ApiResponse(responseCode = "400", description = "Consultation non validable"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> validateConsultation(@PathVariable String id) {
        log.info("Validation de la consultation avec l'ID: {}", id);
        ConsultationDTO validatedConsultation = consultationService.validateConsultation(id);

        return ResponseEntity.ok(validatedConsultation);
    }

    /**
     * Ajoute une prescription de médicament à une consultation.
     *
     * @param consultationId l'identifiant de la consultation
     * @param medicamentId l'identifiant du médicament
     * @param nbPrises nombre de prises du médicament
     * @return ResponseEntity avec la ConsultationDTO mise à jour
     */
    @PostMapping("/{consultationId}/prescriptions")
    @Operation(
            summary = "Ajoute une prescription",
            description = "Ajoute un médicament prescrit à une consultation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prescription ajoutée"),
                    @ApiResponse(responseCode = "404", description = "Consultation ou médicament non trouvé"),
                    @ApiResponse(responseCode = "400", description = "Médicament déjà prescrit"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> addMedicamentPrescription(
            @PathVariable String consultationId,
            @RequestParam String medicamentId,
            @RequestParam Integer nbPrises) {

        log.info("Ajout d'une prescription de médicament {} à la consultation {}", 
                medicamentId, consultationId);
        ConsultationDTO consultation = consultationService.addMedicamentPrescription(
                consultationId, medicamentId, nbPrises);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Supprime une prescription de médicament d'une consultation.
     *
     * @param consultationId l'identifiant de la consultation
     * @param medicamentId l'identifiant du médicament à supprimer
     * @return ResponseEntity avec la ConsultationDTO mise à jour
     */
    @DeleteMapping("/{consultationId}/prescriptions/{medicamentId}")
    @Operation(
            summary = "Supprime une prescription",
            description = "Supprime un médicament prescrit d'une consultation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prescription supprimée"),
                    @ApiResponse(responseCode = "404", description = "Consultation ou prescription non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> removeMedicamentPrescription(
            @PathVariable String consultationId,
            @PathVariable String medicamentId) {

        log.info("Suppression d'une prescription de médicament {} de la consultation {}", 
                medicamentId, consultationId);
        ConsultationDTO consultation = consultationService.removeMedicamentPrescription(
                consultationId, medicamentId);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Met à jour le nombre de prises d'une prescription.
     *
     * @param consultationId l'identifiant de la consultation
     * @param medicamentId l'identifiant du médicament
     * @param nbPrises nouveau nombre de prises
     * @return ResponseEntity avec la ConsultationDTO mise à jour
     */
    @PutMapping("/{consultationId}/prescriptions/{medicamentId}")
    @Operation(
            summary = "Met à jour une prescription",
            description = "Met à jour le nombre de prises d'un médicament prescrit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prescription mise à jour"),
                    @ApiResponse(responseCode = "404", description = "Consultation ou prescription non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> updateMedicamentPrescription(
            @PathVariable String consultationId,
            @PathVariable String medicamentId,
            @RequestParam Integer nbPrises) {

        log.info("Mise à jour d'une prescription de médicament {} de la consultation {}", 
                medicamentId, consultationId);
        ConsultationDTO consultation = consultationService.updateMedicamentPrescription(
                consultationId, medicamentId, nbPrises);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Attache un document (fichier) à une consultation.
     * Le fichier est stocké dans le système de fichiers.
     *
     * @param consultationId l'identifiant de la consultation
     * @param file le fichier à attacher
     * @return ResponseEntity avec la ConsultationDTO mise à jour
     */
    @PostMapping("/{consultationId}/documents")
    @Operation(
            summary = "Attache un document",
            description = "Attache un fichier à une consultation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Document attaché"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "400", description = "Erreur lors de la sauvegarde du fichier"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> attachDocument(
            @PathVariable String consultationId,
            @RequestParam("file") MultipartFile file) {

        log.info("Attachement d'un document à la consultation {}", consultationId);
        ConsultationDTO consultation = consultationService.attachDocument(consultationId, file);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Récupère les médicaments prescrits dans une consultation.
     * Retourne la consultation complète avec tous ses détails incluant les prescriptions.
     *
     * @param consultationId l'identifiant de la consultation
     * @return ResponseEntity avec la ConsultationDTO contenant les prescriptions
     */
    @GetMapping("/{consultationId}/medicaments")
    @Operation(
            summary = "Récupère les médicaments prescrits",
            description = "Retourne la liste des médicaments prescrits pour une consultation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médicaments récupérés"),
                    @ApiResponse(responseCode = "404", description = "Consultation non trouvée"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<ConsultationDTO> getConsultationMedicaments(@PathVariable String consultationId) {
        log.info("Récupération des médicaments prescrits pour la consultation {}", consultationId);
        ConsultationDTO consultation = consultationService.getConsultationMedicaments(consultationId);

        return ResponseEntity.ok(consultation);
    }
}

