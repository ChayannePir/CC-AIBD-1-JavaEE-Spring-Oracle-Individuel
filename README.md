# Projet JEE, Spring, Oracle - API REST de Gestion Médicale

Ce projet est une API REST développée avec Spring Boot 3, Spring Framework 6, Spring Data JPA et Spring Security. Elle permet de gérer des informations médicales telles que les patients, les médecins, les consultations et les prescriptions de médicaments. L'API est conçue pour se connecter à une base de données Oracle XE ou MariaDB, avec une configuration facile.

## Spécifications Fonctionnelles

L'API offre les fonctionnalités suivantes :

*   **Authentification** : Connexion via login/mot de passe et récupération d'un jeton JWT.
*   **Gestion des Patients** :
    *   Récupérer l'ensemble des patients avec pagination et recherche optionnelle (nom ou numéro de sécurité sociale).
*   **Gestion des Médecins** :
    *   Lister avec pagination l'ensemble des médecins, recherche optionnelle (nom ou matricule).
*   **Gestion des Consultations** :
    *   Lister avec pagination l'ensemble des consultations d'un patient donné.
    *   Détailler les médicaments prescrits dans une consultation.
    *   Ajouter, modifier ou supprimer une consultation programmée (à l'avenir).
    *   Modifier la prescription de médicaments d'une consultation.
    *   Possibilité de joindre un document (fichier) à la consultation (fonctionnalité à implémenter).

## Technologies Utilisées

*   **Spring Boot 3.x**
*   **Spring Framework 6.x**
*   **Spring Web** (pour les API REST)
*   **Spring Data JPA** (pour l'accès aux données)
*   **Spring Security** (pour l'authentification et l'autorisation JWT)
*   **JWT (JSON Web Tokens)** (pour la sécurité stateless)
*   **Lombok** (pour réduire le code boilerplate)
*   **Bases de données supportées** : Oracle XE, MariaDB (configurable), H2 (pour le développement/tests)
*   **Maven** (gestion de projet)
*   **Springdoc OpenAPI (Swagger UI)** (pour la documentation de l'API)

## Modèle Conceptuel de Données (MCD)

Le projet est basé sur le MCD suivant, traduit en entités JPA :

*   **MEDICAMENT** (Code, Libellé)
*   **MEDECIN** (Matricule, NomMED)
*   **PATIENT** (N°SS, NomPAT)
*   **CONSULTATION** (Numéro, Date)
    *   Association "Donne" entre `MEDECIN` (0,n) et `CONSULTATION` (1,1)
    *   Association "Assiste" entre `PATIENT` (1,n) et `CONSULTATION` (1,1)
*   **PRESCRIPTION** (NB prises) - Association entre `MEDICAMENT` (0,n) et `CONSULTATION` (0,n)

## Comment démarrer le projet

Suivez ces étapes pour configurer et exécuter le projet localement.

### Prérequis

Assurez-vous d'avoir les éléments suivants installés :

*   **Java Development Kit (JDK) 17 ou plus récent**
*   **Apache Maven 3.x**
*   **Un IDE** (IntelliJ IDEA, Eclipse, VS Code avec extensions Java)
*   **Une base de données** (Oracle XE ou MariaDB) si vous ne souhaitez pas utiliser H2 en mémoire.

### 1. Cloner le dépôt (ou ouvrir le projet existant)

Si vous avez déjà le projet, ouvrez-le dans votre IDE. Sinon, clonez le dépôt Git :

```bash
git clone <URL_DU_DEPOT>
cd CC-AIBD-1-JavaEE-Spring-Oracle-Individuel
```

### 2. Configuration de la base de données

Le fichier `src/main/resources/application.properties` contient les configurations pour différentes bases de données.

*   **H2 (Base de données en mémoire - par défaut pour le développement)** :
    Les lignes H2 sont généralement décommentées par défaut pour faciliter le démarrage.
    ```properties
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.datasource.url=jdbc:h2:mem:mydb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    ```
    Vous pouvez accéder à la console H2 via `http://localhost:8080/h2-console` après le démarrage de l'application.

*   **Oracle XE ou MariaDB** :
    Décommentez la section correspondant à votre base de données et mettez à jour les informations de connexion (URL, nom d'utilisateur, mot de passe).
    **Exemple pour Oracle XE :**
    ```properties
    # spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
    # spring.datasource.username=your_oracle_username
    # spring.datasource.password=your_oracle_password
    # spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
    # spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
    # spring.jpa.hibernate.ddl-auto=update
    ```
    **Exemple pour MariaDB :**
    ```properties
    # spring.datasource.url=jdbc:mariadb://localhost:3306/your_database_name
    # spring.datasource.username=your_mariadb_username
    # spring.datasource.password=your_mariadb_password
    # spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    # spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
    # spring.jpa.hibernate.ddl-auto=update
    ```
    Assurez-vous que `spring.jpa.hibernate.ddl-auto=update` est activé pour que JPA crée ou mette à jour automatiquement le schéma de la base de données au démarrage.

### 3. Exécuter l'application

Vous pouvez exécuter l'application de plusieurs manières :

*   **Depuis votre IDE** :
    Ouvrez la classe `ProjetjeeApplication.java` (située dans `src/main/java/com/example/projetjee/`) et exécutez la méthode `main`.

*   **Avec Maven** :
    Ouvrez un terminal dans le répertoire racine du projet et exécutez :
    ```bash
    mvn spring-boot:run
    ```

*   **En tant que JAR exécutable** :
    1.  Construisez le projet :
        ```bash
        mvn clean install
        ```
    2.  Exécutez le fichier JAR généré :
        ```bash
        java -jar target/projetjee-0.0.1-SNAPSHOT.jar
        ```

L'application démarrera sur `http://localhost:8080` (ou le port configuré dans `application.properties`).

### 4. Accéder à la documentation de l'API (Swagger UI)

Une fois l'application démarrée, vous pouvez accéder à l'interface Swagger UI pour explorer les endpoints de l'API :

*   **URL Swagger UI** : `http://localhost:8080/swagger-ui.html`
*   **URL des définitions OpenAPI** : `http://localhost:8080/v3/api-docs`

### 5. Authentification

L'API utilise JWT pour l'authentification. Vous devrez d'abord vous connecter à l'endpoint d'authentification (à implémenter) pour obtenir un jeton JWT, puis inclure ce jeton dans l'en-tête `Authorization` de vos requêtes (format `Bearer <votre_jeton>`).

---
