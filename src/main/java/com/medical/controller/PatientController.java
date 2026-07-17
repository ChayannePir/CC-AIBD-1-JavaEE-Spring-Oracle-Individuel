package com.medical.controller;

import com.medical.dto.PatientDTO;
import com.medical.service.PatientService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des patients.
 * Fournit les endpoints pour les opérations CRUD et la recherche de patients.
 *
 * Endpoints:
 * - GET /api/patients - Récupère tous les patients avec pagination
 * - GET /api/patients/search - Recherche les patients par critères
 * - GET /api/patients/{id} - Récupère un patient par son ID
 * - POST /api/patients - Crée un nouveau patient
 * - PUT /api/patients/{id} - Met à jour un patient
 * - DELETE /api/patients/{id} - Supprime un patient
 *
 * Sécurité:
 * - Tous les endpoints nécessitent un token JWT valide (@SecurityRequirement)
 * - L'authentification est gérée par Spring Security
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patients", description = "Endpoints pour la gestion des patients")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final PatientService patientService;

    /**
     * Récupère tous les patients avec pagination.
     *
     * Endpoint: GET /api/patients
     * Paramètres de pagination:
     * - page: numéro de page (défaut: 0)
     * - size: nombre de patients par page (défaut: 20)
     * - sort: tri (défaut: nomPAT,asc)
     *
     * Exemple: GET /api/patients?page=0&size=10&sort=nomPAT,asc
     *
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de PatientDTO
     *
     * HTTP 200: Patients récupérés avec succès
     * HTTP 401: Token non fourni ou invalide
     */
    @GetMapping
    @Operation(
            summary = "Récupère tous les patients",
            description = "Retourne une page paginée de tous les patients",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patients récupérés",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @PageableDefault(size = 20, sort = "nomPAT", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Récupération de tous les patients - Page: {}", pageable.getPageNumber());

        Page<PatientDTO> patients = patientService.getAllPatients(pageable);

        return ResponseEntity.ok(patients);
    }

    /**
     * Recherche les patients par critères.
     * Permet une recherche sur le numéro de sécurité sociale et/ou le nom.
     *
     * Endpoint: GET /api/patients/search
     * Paramètres:
     * - numeroSS: numéro de sécurité sociale (optionnel)
     * - nomPAT: nom du patient (optionnel)
     * - page: numéro de page (défaut: 0)
     * - size: nombre de résultats par page (défaut: 20)
     *
     * Exemples:
     * - GET /api/patients/search?numeroSS=1234567890123
     * - GET /api/patients/search?nomPAT=Dupont
     * - GET /api/patients/search?numeroSS=1234567890123&nomPAT=Dupont&page=0&size=10
     *
     * @param numeroSS numéro de sécurité sociale à rechercher (optionnel)
     * @param nomPAT nom du patient à rechercher (optionnel)
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de PatientDTO correspondant aux critères
     *
     * HTTP 200: Patients trouvés
     * HTTP 401: Token non fourni ou invalide
     */
    @GetMapping("/search")
    @Operation(
            summary = "Recherche les patients",
            description = "Recherche les patients par numéro SS et/ou nom avec pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patients trouvés"
                    ),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<PatientDTO>> searchPatients(
            @RequestParam(required = false) String numeroSS,
            @RequestParam(required = false) String nomPAT,
            @PageableDefault(size = 20, sort = "nomPAT", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Recherche de patients - NumeroSS: {}, NomPAT: {}", numeroSS, nomPAT);

        Page<PatientDTO> patients = patientService.searchPatients(numeroSS, nomPAT, pageable);

        return ResponseEntity.ok(patients);
    }

    /**
     * Récupère un patient spécifique par son ID.
     *
     * Endpoint: GET /api/patients/{id}
     * Paramètre d'URL:
     * - id: l'identifiant MongoDB du patient
     *
     * Exemple: GET /api/patients/507f1f77bcf86cd799439011
     *
     * @param id l'identifiant du patient
     * @return ResponseEntity avec PatientDTO
     *
     * HTTP 200: Patient récupéré
     * HTTP 404: Patient non trouvé
     * HTTP 401: Token non fourni ou invalide
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupère un patient par ID",
            description = "Retourne les détails d'un patient spécifique",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patient trouvé",
                            content = @Content(schema = @Schema(implementation = PatientDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable String id) {
        log.info("Récupération du patient avec l'ID: {}", id);

        PatientDTO patient = patientService.getPatientById(id);

        return ResponseEntity.ok(patient);
    }

    /**
     * Crée un nouveau patient.
     *
     * Endpoint: POST /api/patients
     * Corps de la requête:
     * {
     *   "numeroSS": "1234567890123",
     *   "nomPAT": "Jean Dupont"
     * }
     *
     * @param patientDTO données du patient à créer
     * @return ResponseEntity avec le PatientDTO créé
     *
     * HTTP 201: Patient créé avec succès
     * HTTP 409: Un patient avec ce numero SS existe déjà
     * HTTP 401: Token non fourni ou invalide
     */
    @PostMapping
    @Operation(
            summary = "Crée un nouveau patient",
            description = "Crée un nouveau patient avec le numéro SS fourni",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patient créé",
                            content = @Content(schema = @Schema(implementation = PatientDTO.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Patient déjà existant"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        log.info("Création d'un nouveau patient avec le numéro SS: {}", patientDTO.getNumeroSS());

        PatientDTO createdPatient = patientService.createPatient(patientDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    /**
     * Met à jour un patient existant.
     *
     * Endpoint: PUT /api/patients/{id}
     * Paramètre d'URL:
     * - id: l'identifiant du patient à mettre à jour
     * Corps de la requête: Données mises à jour du patient
     *
     * Exemple: PUT /api/patients/507f1f77bcf86cd799439011
     * {
     *   "numeroSS": "1234567890123",
     *   "nomPAT": "Jean Dupont Modifié"
     * }
     *
     * @param id l'identifiant du patient
     * @param patientDTO données mises à jour
     * @return ResponseEntity avec le PatientDTO mis à jour
     *
     * HTTP 200: Patient mis à jour
     * HTTP 404: Patient non trouvé
     * HTTP 401: Token non fourni ou invalide
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Met à jour un patient",
            description = "Met à jour les données d'un patient existant",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patient mis à jour",
                            content = @Content(schema = @Schema(implementation = PatientDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable String id,
            @RequestBody PatientDTO patientDTO) {

        log.info("Mise à jour du patient avec l'ID: {}", id);

        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);

        return ResponseEntity.ok(updatedPatient);
    }

    /**
     * Supprime un patient.
     *
     * Endpoint: DELETE /api/patients/{id}
     * Paramètre d'URL:
     * - id: l'identifiant du patient à supprimer
     *
     * Exemple: DELETE /api/patients/507f1f77bcf86cd799439011
     *
     * @param id l'identifiant du patient
     * @return ResponseEntity vide avec status 204
     *
     * HTTP 204: Patient supprimé
     * HTTP 404: Patient non trouvé
     * HTTP 401: Token non fourni ou invalide
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprime un patient",
            description = "Supprime un patient et toutes ses données associées",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Patient supprimé"
                    ),
                    @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        log.info("Suppression du patient avec l'ID: {}", id);

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }
}

