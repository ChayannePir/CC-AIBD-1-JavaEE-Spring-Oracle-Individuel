package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) pour les réponses d'authentification.
 * Utilisé pour retourner le token JWT et les informations utilisateur après connexion.
 *
 * Attributs:
 * - token: Token JWT pour authentifier les requêtes suivantes
 * - username: Nom d'utilisateur connecté
 * - email: Email de l'utilisateur
 * - roles: Rôles/permissions de l'utilisateur
 * - type: Type de token (Bearer)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Réponse d'authentification contenant le token JWT")
public class LoginResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Token JWT pour l'authentification
     * À inclure dans le header Authorization: Bearer <token>
     */
    @Schema(description = "Token JWT pour l'authentification des requêtes", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    /**
     * Nom d'utilisateur connecté
     */
    @Schema(description = "Nom d'utilisateur connecté", example = "doctor_001")
    private String username;

    /**
     * Email de l'utilisateur
     */
    @Schema(description = "Email de l'utilisateur", example = "doctor@medical.com")
    private String email;

    /**
     * Rôles de l'utilisateur
     */
    @Schema(description = "Rôles de l'utilisateur", example = "ROLE_DOCTOR")
    private String roles;

    /**
     * Type de token (toujours "Bearer")
     */
    @Schema(description = "Type de token", example = "Bearer")
    private String type;
}

