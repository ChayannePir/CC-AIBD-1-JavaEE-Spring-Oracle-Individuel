package com.medical.controller;

import com.medical.dto.MedicamentDTO;
import com.medical.service.MedicamentService;
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
 * Contrôleur REST pour la gestion des médicaments.
 * Fournit les endpoints pour les opérations CRUD et la recherche de médicaments.
 *
 * Endpoints:
 * - GET /api/medicaments - Récupère tous les médicaments avec pagination
 * - GET /api/medicaments/search - Recherche les médicaments par critères
 * - GET /api/medicaments/{id} - Récupère un médicament par son ID
 * - POST /api/medicaments - Crée un nouveau médicament
 * - PUT /api/medicaments/{id} - Met à jour un médicament
 * - DELETE /api/medicaments/{id} - Supprime un médicament
 */
@RestController
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Médicaments", description = "Endpoints pour la gestion des médicaments")
@SecurityRequirement(name = "bearerAuth")
public class MedicamentController {

    private final MedicamentService medicamentService;

    /**
     * Récupère tous les médicaments avec pagination.
     *
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de MedicamentDTO
     */
    @GetMapping
    @Operation(
            summary = "Récupère tous les médicaments",
            description = "Retourne une page paginée de tous les médicaments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médicaments récupérés"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<MedicamentDTO>> getAllMedicaments(
            @PageableDefault(size = 20, sort = "libelle", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Récupération de tous les médicaments");
        Page<MedicamentDTO> medicaments = medicamentService.getAllMedicaments(pageable);

        return ResponseEntity.ok(medicaments);
    }

    /**
     * Recherche les médicaments par critères (libellé).
     *
     * @param libelle libellé du médicament (optionnel)
     * @param pageable paramètres de pagination
     * @return ResponseEntity avec une page de MedicamentDTO
     */
    @GetMapping("/search")
    @Operation(
            summary = "Recherche les médicaments",
            description = "Recherche les médicaments par libellé avec pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médicaments trouvés"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Page<MedicamentDTO>> searchMedicaments(
            @RequestParam(required = false) String libelle,
            @PageableDefault(size = 20, sort = "libelle", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("Recherche de médicaments - Libelle: {}", libelle);
        Page<MedicamentDTO> medicaments = medicamentService.searchMedicaments(libelle, pageable);

        return ResponseEntity.ok(medicaments);
    }

    /**
     * Récupère un médicament spécifique par son ID.
     *
     * @param id l'identifiant du médicament
     * @return ResponseEntity avec MedicamentDTO
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupère un médicament par ID",
            description = "Retourne les détails d'un médicament spécifique",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médicament trouvé"),
                    @ApiResponse(responseCode = "404", description = "Médicament non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedicamentDTO> getMedicamentById(@PathVariable String id) {
        log.info("Récupération du médicament avec l'ID: {}", id);
        MedicamentDTO medicament = medicamentService.getMedicamentById(id);

        return ResponseEntity.ok(medicament);
    }

    /**
     * Crée un nouveau médicament.
     *
     * @param medicamentDTO données du médicament à créer
     * @return ResponseEntity avec le MedicamentDTO créé
     */
    @PostMapping
    @Operation(
            summary = "Crée un nouveau médicament",
            description = "Crée un nouveau médicament avec le code fourni",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Médicament créé"),
                    @ApiResponse(responseCode = "409", description = "Médicament déjà existant"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedicamentDTO> createMedicament(@RequestBody MedicamentDTO medicamentDTO) {
        log.info("Création d'un nouveau médicament avec le code: {}", medicamentDTO.getCode());
        MedicamentDTO createdMedicament = medicamentService.createMedicament(medicamentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicament);
    }

    /**
     * Met à jour un médicament existant.
     *
     * @param id l'identifiant du médicament
     * @param medicamentDTO données mises à jour
     * @return ResponseEntity avec le MedicamentDTO mis à jour
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Met à jour un médicament",
            description = "Met à jour les données d'un médicament existant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Médicament mis à jour"),
                    @ApiResponse(responseCode = "404", description = "Médicament non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<MedicamentDTO> updateMedicament(
            @PathVariable String id,
            @RequestBody MedicamentDTO medicamentDTO) {

        log.info("Mise à jour du médicament avec l'ID: {}", id);
        MedicamentDTO updatedMedicament = medicamentService.updateMedicament(id, medicamentDTO);

        return ResponseEntity.ok(updatedMedicament);
    }

    /**
     * Supprime un médicament.
     *
     * @param id l'identifiant du médicament
     * @return ResponseEntity vide avec status 204
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprime un médicament",
            description = "Supprime un médicament",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Médicament supprimé"),
                    @ApiResponse(responseCode = "404", description = "Médicament non trouvé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            }
    )
    public ResponseEntity<Void> deleteMedicament(@PathVariable String id) {
        log.info("Suppression du médicament avec l'ID: {}", id);
        medicamentService.deleteMedicament(id);

        return ResponseEntity.noContent().build();
    }
}

