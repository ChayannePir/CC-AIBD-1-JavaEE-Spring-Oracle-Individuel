package com.medical.security;

import com.medical.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre JWT personnalisé pour Spring Security.
 * Intercepte chaque requête HTTP pour:
 * 1. Extraire le token JWT du header Authorization
 * 2. Valider le token
 * 3. Charger les détails de l'utilisateur
 * 4. Configurer le contexte de sécurité Spring Security
 *
 * Hérite de OncePerRequestFilter pour s'assurer qu'il est exécuté une seule fois par requête.
 *
 * Header attendu: Authorization: Bearer <jwt_token>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    /**
     * Préfixe du token dans le header Authorization
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Filtre une requête HTTP pour valider et traiter le token JWT.
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @param filterChain la chaîne de filtres
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extraire le token JWT du header Authorization
            String jwt = extractJwtFromRequest(request);

            // Si un token est trouvé et valide, authentifier l'utilisateur
            if (jwt != null && authService.validateToken(jwt)) {
                // Extraire le username du token
                String username = authService.extractUsername(jwt);

                // Charger les détails de l'utilisateur
                UserDetails userDetails = authService.loadUserByUsername(username);

                // Créer l'authentification token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Configurer les détails de la requête
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Définir l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Utilisateur {} authentifié par JWT", username);
            }

        } catch (Exception e) {
            log.error("Erreur lors du traitement du token JWT: {}", e.getMessage());
        }

        // Continuer avec la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT du header Authorization.
     * Format attendu: Authorization: Bearer <token>
     *
     * @param request la requête HTTP
     * @return le token JWT sans le préfixe "Bearer ", ou null si non trouvé
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // Récupérer le header Authorization
        String bearerToken = request.getHeader("Authorization");

        // Vérifier que le header existe et commence par "Bearer "
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            // Retourner le token sans le préfixe
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}

