package com.medical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe principale de l'application Spring Boot pour l'API Médicale.
 *
 * Annotations:
 * - @SpringBootApplication: Active Spring Boot avec:
 *   - @Configuration: Indique que c'est une classe de configuration
 *   - @ComponentScan: Scanne les composants dans le package actuel et sous-packages
 *   - @EnableAutoConfiguration: Active l'auto-configuration de Spring Boot
 *
 * Point d'entrée:
 * - Exécution: mvn spring-boot:run
 * - L'application démarre sur http://localhost:8080
 *
 * Documentation Swagger:
 * - http://localhost:8080/swagger-ui.html
 *
 * Dépendances principales:
 * - Spring Web: Crée les contrôleurs REST
 * - Spring Data MongoDB: Accès à MongoDB
 * - Spring Security: Authentification et autorisation
 * - JWT (JJWT): Gestion des tokens
 * - Lombok: Réduit la verbosité du code
 */
@SpringBootApplication
public class MedicalApiApplication {

    /**
     * Point d'entrée de l'application.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(MedicalApiApplication.class, args);
    }
}

