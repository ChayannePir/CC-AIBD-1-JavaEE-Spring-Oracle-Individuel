package com.medical.config;

import com.medical.security.JwtAuthenticationFilter;
import com.medical.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration de Spring Security pour l'API REST.
 * Définit:
 * - Les règles d'accès aux endpoints
 * - La gestion de l'authentification avec JWT
 * - CORS pour les requêtes cross-origin
 * - CSRF (désactivé pour l'API REST)
 * - La politique de session (stateless avec JWT)
 *
 * Annotations:
 * - @Configuration: Indique que c'est une configuration Spring
 * - @EnableWebSecurity: Active la sécurité web
 * - @EnableMethodSecurity: Active la sécurité au niveau des méthodes (@PreAuthorize, @PostAuthorize)
 * - @RequiredArgsConstructor: Génère le constructeur avec dépendances (Lombok)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Crée un AuthenticationManager pour gérer l'authentification.
     *
     * @param http la configuration HTTP Security
     * @return le AuthenticationManager configuré
     * @throws Exception exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    /**
     * Crée un encodeur de mot de passe utilisant BCrypt.
     * BCrypt est un algorithme de hachage sécurisé qui ajoute du "salt" automatiquement.
     *
     * @return un PasswordEncoder BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * Définit les règles d'accès, la gestion CORS, CSRF, et l'intégration du filtre JWT.
     *
     * @param http la configuration HttpSecurity
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST API (used with JWT tokens instead)
                .csrf()
                .disable()

                // Enable CORS
                .and()
                .cors()

                // Use stateless session (no server-side session storage)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // Authorization configuration
                .and()
                .authorizeHttpRequests()
                // Public endpoints - no authentication required
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // Swagger/OpenAPI documentation - public access
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()

                // Add custom JWT filter before default UsernamePasswordAuthenticationFilter
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configure CORS (Cross-Origin Resource Sharing).
     * Permet aux requêtes en provenance d'autres origines d'accéder à l'API.
     *
     * Configuration:
     * - Origines autorisées: localhost:3000, localhost:4200, etc. (à adapter selon les besoins)
     * - Méthodes autorisées: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * - Headers autorisés: tous
     * - Credentials: autorisés (pour les cookies/tokens)
     *
     * @return la source de configuration CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Spécifier les origines autorisées
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // React développement
                "http://localhost:4200",      // Angular développement
                "http://localhost:8080",      // Local testing
                "http://127.0.0.1:3000",
                "http://127.0.0.1:4200"
        ));

        // Spécifier les méthodes HTTP autorisées
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Spécifier les headers autorisés
        configuration.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Accept"
        ));

        // Permettre les credentials (cookies, headers d'autorisation)
        configuration.setAllowCredentials(true);

        // Durée de cache en secondes (3600 = 1 heure)
        configuration.setMaxAge(3600L);

        // Créer et retourner la configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

