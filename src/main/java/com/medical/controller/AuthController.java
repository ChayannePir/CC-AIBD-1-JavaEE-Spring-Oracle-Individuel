package com.medical.controller;

import com.medical.dto.LoginRequestDTO;
import com.medical.dto.LoginResponseDTO;
import com.medical.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'authentification.
 * Fournit les endpoints pour l'authentification des utilisateurs.
 *
 * Endpoints:
 * - POST /api/auth/login - Connexion avec JWT
 *
 * Annotations:
 * - @RestController: Indique que c'est un contrôleur REST (retourne du JSON)
 * - @RequestMapping: Préfixe des URL pour tous les endpoints (/api/auth)
 * - @RequiredArgsConstructor: Génère le constructeur avec dépendances (Lombok)
 * - @Slf4j: Génère un logger (Lombok)
 * - @Tag: Documentation OpenAPI/Swagger
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des tokens JWT")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint de connexion.
     * Accepte un username et password, retourne un token JWT.
     *
     * Endpoint: POST /api/auth/login
     * Corps: {"username": "user", "password": "pass"}
     * Réponse: {"token": "eyJ...", "username": "user", "email": "user@example.com", "roles": "ROLE_ADMIN", "type": "Bearer"}
     *
     * @param loginRequest contient le username et password
     * @return ResponseEntity avec LoginResponseDTO contenant le token JWT
     *
     * HTTP 200: Connexion réussie, token retourné
     * HTTP 401: Identifiants incorrects
     */
    @PostMapping("/login")
    @Operation(
            summary = "Connexion utilisateur",
            description = "Authentifie un utilisateur et retourne un token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connexion réussie",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Identifiants incorrects"
                    )
            }
    )
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Tentative de connexion pour l'utilisateur: {}", loginRequest.getUsername());

        // Appeler le service d'authentification
        LoginResponseDTO response = authService.login(loginRequest);

        log.info("Connexion réussie pour: {}", loginRequest.getUsername());

        return ResponseEntity.ok(response);
    }
}

