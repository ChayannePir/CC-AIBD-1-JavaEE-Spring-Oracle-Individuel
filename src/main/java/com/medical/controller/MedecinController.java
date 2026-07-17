package com.medical.controller;

import com.medical.dto.MedecinDTO;
import com.medical.service.MedecinService;
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

/**
 * Contrôleur REST pour la gestion des médecins.
 * Fournit les endpoints pour les opérations CRUD et la recherche de médecins.
 *
 * Endpoints:
 * - GET /api/medecins - Récupère tous les médecins avec pagination
 * - GET /api/medecins/search - Recherche les médecins par critères
 * - GET /api/medecins/{id} - Récupère un médecin par son ID
 * - POST /api/medecins - Crée un nouveau médecin
 * - PUT /api/medecins/{id} - Met à jour un médecin
 * - DELETE /api/medecins/{id} - Supprime un médecin
 */
@RestController
@RequestMapping("/api/medecins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Médecins", description = "Endpoints pour la gestion des médecins")
@SecurityRequirement(name = "bearerAuth")
public class MedecinController {

    private final MedecinService medecinService;

    /**
     * Récupère tous les médecins avec pagination.
     *
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de MedecinDTO
     */
    @GetMapping
    @Operation(
            summary = "Récupère tous les médecins",
            description = "Retourne une page paginée de tous les médecins",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médecins récupérés"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<MedecinDTO>> getAllMedecins(
            @PageableDefault(size = 20, sort = "nomMED", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Récupération de tous les médecins");
        Page<MedecinDTO> medecins = medecinService.getAllMedecins(pageable);

        return ResponseEntity.ok(medecins);
    }

    /**
     * Recherche les médecins par critères (matricule et/ou nom).
     *
     * Paramètres:
     * - matricule: matricule du médecin (optionnel)
     * - nomMED: nom du médecin (optionnel)
     * - page: numéro de page
     * - size: nombre de résultats par page
     *
     * @param matricule matricule du médecin (optionnel)
     * @param nomMED nom du médecin (optionnel)
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de MedecinDTO
     */
    @GetMapping("/search")
    @Operation(
            summary = "Recherche les médecins",
            description = "Recherche les médecins par matricule et/ou nom avec pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médecins trouvés"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<MedecinDTO>> searchMedecins(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nomMED,
            @PageableDefault(size = 20, sort = "nomMED", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Recherche de médecins - Matricule: {}, NomMED: {}", matricule, nomMED);
        Page<MedecinDTO> medecins = medecinService.searchMedecins(matricule, nomMED, pageable);

        return ResponseEntity.ok(medecins);
    }

    /**
     * Récupère un médecin spécifique par son ID.
     *
     * @param id l'identifiant du médecin
     * @return ResponseEntity avec MedecinDTO
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupère un médecin par ID",
            description = "Retourne les détails d'un médecin spécifique",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médecin trouvé"),
                    @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedecinDTO> getMedecinById(@PathVariable String id) {
        log.info("Récupération du médecin avec l'ID: {}", id);
        MedecinDTO medecin = medecinService.getMedecinById(id);

        return ResponseEntity.ok(medecin);
    }

    /**
     * Crée un nouveau médecin.
     *
     * @param medecinDTO données du médecin à créer
     * @return ResponseEntity avec le MedecinDTO créé
     */
    @PostMapping
    @Operation(
            summary = "Crée un nouveau médecin",
            description = "Crée un nouveau médecin avec le matricule fourni",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Médecin créé"),
                    @ApiResponse(responseCode = "409", description = "Médecin déjà existant"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedecinDTO> createMedecin(@RequestBody MedecinDTO medecinDTO) {
        log.info("Création d'un nouveau médecin avec le matricule: {}", medecinDTO.getMatricule());
        MedecinDTO createdMedecin = medecinService.createMedecin(medecinDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedecin);
    }

    /**
     * Met à jour un médecin existant.
     *
     * @param id l'identifiant du médecin
     * @param medecinDTO données mises à jour
     * @return ResponseEntity avec le MedecinDTO mis à jour
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Met à jour un médecin",
            description = "Met à jour les données d'un médecin existant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médecin mis à jour"),
                    @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedecinDTO> updateMedecin(
            @PathVariable String id,
            @RequestBody MedecinDTO medecinDTO) {

        log.info("Mise à jour du médecin avec l'ID: {}", id);
        MedecinDTO updatedMedecin = medecinService.updateMedecin(id, medecinDTO);

        return ResponseEntity.ok(updatedMedecin);
    }

    /**
     * Supprime un médecin.
     *
     * @param id l'identifiant du médecin
     * @return ResponseEntity vide avec status 204
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprime un médecin",
            description = "Supprime un médecin",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Médecin supprimé"),
                    @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Void> deleteMedecin(@PathVariable String id) {
        log.info("Suppression du médecin avec l'ID: {}", id);
        medecinService.deleteMedecin(id);

        return ResponseEntity.noContent().build();
    }
}

