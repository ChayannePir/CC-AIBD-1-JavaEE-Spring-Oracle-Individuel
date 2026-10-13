# Medical API - Spring Boot 3 + MongoDB

API REST pour la gestion des patients, médecins, consultations, prescriptions médicales et documents associés.

Cette application a été validée avec Spring Boot 3.2.1, Java 17, Spring Security, MongoDB, JWT et Swagger/OpenAPI.

## Objectif

Projet de service REST médical proposant :
- authentification login/password avec JWT
- gestion des patients, médecins, médicaments et consultations
- pagination et recherche par critères
- gestion des prescriptions de médicaments
- ajout de documents à une consultation
- sécurité Spring Security
- documentation Swagger
- tests Spring Boot

## Stack technique

- Java 17
- Spring Boot 3.2.1
- Spring Web
- Spring Data MongoDB
- Spring Security
- JWT (jjwt 0.12.3)
- SpringDoc OpenAPI 2.3.0
- Maven
- JUnit 5 / Spring Boot Test

## Architecture

Le projet suit une architecture en couches :
- controller : endpoints REST
- service : logique métier
- repository : accès MongoDB
- entity : entités du domaine
- dto : objets de transfert
- exception : gestion centralisée des erreurs
- security : JWT + Spring Security
- config : configuration Spring

## Prérequis

Avant de lancer le projet, vérifier :
- Java 17+
- Maven 3.8+
- MongoDB démarré localement sur localhost:27017

Vérification rapide :

```powershell
java -version
mvn -version
```

## Installation

```powershell
cd C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel
mvn clean install
```

## Configuration

Fichier principal :

```text
src/main/resources/application.properties
```

Configuration MongoDB utilisée par défaut :

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/medical_db
```

Configuration JWT :

```properties
jwt.secret=mySecretKeyForJWTSigningThatIsAtLeast32CharactersLongAndStronger
jwt.expiration=86400000
```

## Lancement

Lancer l’application :

```powershell
mvn spring-boot:run
```

L’API sera disponible sur :

```text
http://localhost:8080
```

La documentation Swagger est disponible sur :

```text
http://localhost:8080/swagger-ui.html
```

## Authentification

Endpoint de connexion :

```http
POST /api/auth/login
```

Exemple de body :

```json
{
  "username": "doctor_001",
  "password": "doctor123"
}
```

Exemple de réponse :

```json
{
  "token": "...",
  "username": "doctor_001",
  "email": "doctor@medical.com",
  "roles": "ROLE_DOCTOR",
  "type": "Bearer"
}
```

Puis utiliser le header :

```http
Authorization: Bearer <token>
```

## Endpoints principaux

### Authentification
- POST /api/auth/login

### Patients
- GET /api/patients
- GET /api/patients/search
- GET /api/patients/{id}
- POST /api/patients
- PUT /api/patients/{id}
- DELETE /api/patients/{id}

### Médecins
- GET /api/medecins
- GET /api/medecins/search
- GET /api/medecins/{id}
- POST /api/medecins
- PUT /api/medecins/{id}
- DELETE /api/medecins/{id}

### Consultations
- GET /api/consultations
- GET /api/consultations/{id}
- GET /api/consultations/patient/{patientId}
- POST /api/consultations
- PUT /api/consultations/{id}
- DELETE /api/consultations/{id}
- PATCH /api/consultations/{id}/validate
- POST /api/consultations/{consultationId}/prescriptions
- DELETE /api/consultations/{consultationId}/prescriptions/{medicamentId}
- PUT /api/consultations/{consultationId}/prescriptions/{medicamentId}
- POST /api/consultations/{id}/documents
- GET /api/consultations/{id}/medicaments

### Médicaments
- GET /api/medicaments
- GET /api/medicaments/search
- GET /api/medicaments/{id}
- POST /api/medicaments
- PUT /api/medicaments/{id}
- DELETE /api/medicaments/{id}

## Sécurité

La configuration Spring Security est active avec JWT.

- CSRF désactivé pour l’API REST
- sessions sans état (STATELESS)
- CORS configuré pour localhost
- endpoints publics : login + swagger
- endpoints protégés : tout le reste

## Tests

Exécuter tous les tests :

```powershell
mvn test
```

Exécuter un test ciblé :

```powershell
mvn test -Dtest=AuthControllerTest
```

## Commandes utiles

```powershell
mvn clean
mvn compile
mvn test
mvn package
mvn spring-boot:run
```

## MongoDB

Assurez-vous que MongoDB est démarré :

```powershell
mongod
```

Puis vérifier :

```powershell
mongosh "mongodb://localhost:27017/medical_db"
```

## Vérification de démarrage

L’application est considérée comme démarrée correctement lorsqu’on voit dans la console :

```text
Tomcat started on port 8080
Started MedicalApiApplication
```

## Remarques

- Le projet est configuré pour MongoDB.
- Il est prêt pour une démonstration locale et une présentation orale.
- Le cycle Spring Security / JWT a été corrigé pour éviter l’erreur de dépendance circulaire.

## État du projet

Le projet est fonctionnel et compilable sous Java 17 avec Spring Boot 3.2.x et MongoDB local.
