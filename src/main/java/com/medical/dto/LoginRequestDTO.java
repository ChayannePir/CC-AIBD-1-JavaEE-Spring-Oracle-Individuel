package com.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO pour la demande de login.
 * Reçoit les credentials de l'utilisateur pour l'authentification.
 *
 * Attributs:
 * - username: Nom d'utilisateur
 * - password: Mot de passe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Demande de connexion avec identifiants")
public class LoginRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nom d'utilisateur pour la connexion
     */
    @Schema(description = "Nom d'utilisateur", example = "doctor_001", required = true)
    private String username;

    /**
     * Mot de passe pour la connexion
     */
    @Schema(description = "Mot de passe", example = "password123", required = true)
    private String password;
}

