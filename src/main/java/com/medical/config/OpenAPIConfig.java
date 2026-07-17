package com.medical.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API REST.
 * Génère automatiquement la documentation interactive à:
 * - http://localhost:8080/swagger-ui.html
 * - http://localhost:8080/v3/api-docs (format JSON)
 *
 * Cette configuration définit:
 * - Les informations de base de l'API (titre, version, description)
 * - Le schéma de sécurité (Bearer JWT)
 * - Les contactes et la license
 *
 * Pour tester l'API:
 * 1. Ouvrir http://localhost:8080/swagger-ui.html
 * 2. Cliquer sur l'endpoint /api/auth/login et essayer
 * 3. Utiliser le token récupéré dans les autres endpoints
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Crée et configure le OpenAPI (Swagger 3) pour l'API.
     *
     * @return la configuration OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Informations générales de l'API
                .info(new Info()
                        .title("Medical API - REST Service")
                        .version("1.0.0")
                        .description("API REST pour la gestion des patients, médecins, consultations et prescriptions médecales")
                        .termsOfService("https://example.com/terms")

                        // Informations de contact
                        .contact(new Contact()
                                .name("Support technique")
                                .email("support@medical-api.fr")))

                        // Informations de license
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))

                // Configuration du schéma de sécurité JWT
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))

                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT pour l'authentification. " +
                                                "Récupérez un token via POST /api/auth/login " +
                                                "puis utilisez-le dans les headers: Authorization: Bearer <token>")));
    }
}

