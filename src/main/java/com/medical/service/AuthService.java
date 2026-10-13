package com.medical.service;

import com.medical.dto.LoginRequestDTO;
import com.medical.dto.LoginResponseDTO;

/**
 * Interface de service pour l'authentification et la gestion des utilisateurs.
 * Gère la connexion, l'enregistrement et la validation des tokens JWT.
 */
public interface AuthService {

    /**
     * Effectue la connexion d'un utilisateur avec username et password.
     * Retourne un token JWT si l'authentification réussit.
     *
     * @param loginRequest contient le username et le password
     * @return LoginResponseDTO contenant le token JWT et les informations de l'utilisateur
     * @throws BadCredentialsException si les identifiants sont incorrects
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    /**
     * Extrait le username du token JWT.
     *
     * @param token le token JWT
     * @return le username encodé dans le token
     */
    String extractUsername(String token);

    /**
     * Valide si un token JWT est valide et non expiré.
     *
     * @param token le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    boolean validateToken(String token);

    /**
     * Charge les détails d'un utilisateur par son username.
     * Utilisé par Spring Security pour l'authentification.
     *
     * @param username le nom d'utilisateur
     * @return les détails de l'utilisateur
     */
    org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username);
}

