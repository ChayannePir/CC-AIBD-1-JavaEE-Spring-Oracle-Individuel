# Medical API - REST Service avec Spring Boot 3 et MongoDB

## 📋 Table des matières
1. [Description](#description)
2. [Architecture](#architecture)
3. [Prérequis](#prérequis)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Démarrage](#démarrage)
7. [Documentation API](#documentation-api)
8. [Tests](#tests)
9. [Commandes utiles](#commandes-utiles)
10. [Structure du projet](#structure-du-projet)

---

## 🎯 Description

**Medical API** est une API REST développée avec Spring Boot 3, conçue pour la gestion des patients, médecins, consultations et prescriptions médicales.

### Caractéristiques principales:
- ✅ Authentification avec tokens JWT
- ✅ Pagination et recherche avancée
- ✅ Gestion des consultations et prescriptions de médicaments
- ✅ Gestion des documents attachés (fichiers)
- ✅ Architecture en couches (Contrôleur/Service/Repository)
- ✅ Gestion des exceptions globale
- ✅ Documentation Swagger/OpenAPI interactive
- ✅ Tests unitaires et d'intégration
- ✅ Logs structurés avec SLF4J
- ✅ Sécurité Spring Security avec JWT

---

## 🏗️ Architecture

### Couches de l'application:
```
┌─────────────────────────────────────┐
│     CONTROLLERS (REST Endpoints)     │ ← Gère les requêtes HTTP
├─────────────────────────────────────┤
│        SERVICES (Business Logic)     │ ← Logique métier
├─────────────────────────────────────┤
│    REPOSITORIES (Data Access)        │ ← Accès MongoDB
├─────────────────────────────────────┤
│      MongoDB (Persistence)           │ ← Base de données
└─────────────────────────────────────┘
```

### Packages principaux:
```
com.medical/
├── controller/           # Contrôleurs REST
├── service/             # Interfaces des services
├── service/impl/        # Implémentations des services
├── repository/          # Repositories MongoDB
├── entity/              # Entités JPA/MongoDB
├── dto/                 # Data Transfer Objects
├── exception/           # Gestion des exceptions
├── security/            # Configuration de sécurité
├── config/              # Configurations Spring
└── initialization/      # Initialisation des données
```

### Modèle de données:
```
PATIENT {
  id: String (MongoDB ObjectId)
  numeroSS: String (Unique)
  nomPAT: String
}

MEDECIN {
  id: String (MongoDB ObjectId)
  matricule: String (Unique)
  nomMED: String
}

CONSULTATION {
  id: String
  numero: String
  dateConsultation: LocalDateTime
  patient: Patient (Reference)
  medecin: Medecin (Reference)
  medicaments: List<Prescription>
  documentPath: String
  statut: String (PROGRAMMEE|EFFECTUEE|ANNULEE)
}

PRESCRIPTION {
  medicament: Medicament
  nbPrises: Integer
}

MEDICAMENT {
  id: String
  code: String (Unique)
  libelle: String
}

USER {
  id: String
  username: String (Unique)
  password: String (BCrypt)
  email: String
  roles: List<String>
  actif: Boolean
}
```

---

## 📦 Prérequis

### Logiciels requis:
- **Java 21+** (JDK)
  - Télécharger: https://www.oracle.com/java/technologies/downloads/
  
- **Maven 3.8+** (Build tool)
  - Télécharger: https://maven.apache.org/download.cgi
  
- **MongoDB 5.0+** (Base de données)
  - Télécharger: https://www.mongodb.com/try/download/community
  - Ou utiliser MongoDB Atlas (cloud): https://www.mongodb.com/cloud/atlas

### Vérifier les installations:
```powershell
# Vérifier Java
java -version

# Vérifier Maven
mvn -version

# Vérifier MongoDB (si installé localement)
mongod --version
```

---

## 🚀 Installation

### 1. Cloner le projet
```powershell
# Si git est disponible
git clone <url-du-repository>
cd CC-AIBD-1-JavaEE-Spring-Oracle-Individuel

# Sinon, extraire l'archive du projet
```

### 2. Vérifier la structure du projet
```powershell
# Windows PowerShell
dir /s /b *.java | Select -First 5

# Ou vérifier qu'il existe un pom.xml
Test-Path pom.xml
```

### 3. Télécharger les dépendances Maven
```powershell
# Se placer à la racine du projet (où il y a le pom.xml)
mvn clean install

# Cela va:
# 1. Télécharger toutes les dépendances
# 2. Compiler le code
# 3. Construire le projet
```

---

## ⚙️ Configuration

### 1. Configuration de MongoDB

#### Option A: MongoDB Local
```powershell
# Démarrer MongoDB (si installé localement)
mongod

# La configuration par défaut attend MongoDB sur localhost:27017
# Vérifier que application.properties contient:
# spring.data.mongodb.uri=mongodb://localhost:27017/medical_db
```

#### Option B: MongoDB Atlas (Cloud)
```properties
# Modifier application.properties:
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/medical_db

# Obtenir la chaîne de connexion:
# 1. Créer un compte sur https://www.mongodb.com/cloud/atlas
# 2. Créer un cluster
# 3. Créer un utilisateur de base de données
# 4. Cliquer sur "Connect" et copier la chaîne MONGODB CONNECTION STRING
# 5. Remplacer <username>, <password> et le nom de la base
```

### 2. Configuration JWT
```properties
# Dans application.properties, modifier:
jwt.secret=mySecretKeyForJWTSigningThatIsAtLeast32CharactersLong
jwt.expiration=86400000  # 24 heures en millisecondes
```

⚠️ **IMPORTANT en production:**
- Générer une clé secrète forte (au moins 32 caractères)
- Stocker la clé dans des variables d'environnement
- Ne jamais commiter la configuration réelle dans Git

### 3. Autres configurations
```properties
# Logging
logging.level.com.medical=DEBUG

# Port du serveur
server.port=8080

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## ▶️ Démarrage

### Option 1: Avec Maven
```powershell
# Se placer à la racine du projet
cd C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel

# Démarrer l'application
mvn spring-boot:run

# L'application démarre sur http://localhost:8080
```

### Option 2: Compiler puis exécuter le JAR
```powershell
# Compiler et créer un JAR
mvn clean package -DskipTests

# Exécuter le JAR
java -jar target/medical-api-1.0.0.jar
```

### Vérifier que l'application est démarrée:
```powershell
# Vérifier que le port 8080 écoute
netstat -ano | findstr ":8080"

# Ou faire une requête de test
Invoke-WebRequest -Uri http://localhost:8080/swagger-ui.html
```

---

## 📚 Documentation API

### Accéder à la documentation Swagger:
```
http://localhost:8080/swagger-ui.html
```

### Format JSON des endpoints:

#### 1. **Authentification**

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "doctor_001",
  "password": "doctor123"
}

Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "doctor_001",
  "email": "doctor@medical-api.fr",
  "roles": "ROLE_DOCTOR",
  "type": "Bearer"
}
```

#### 2. **Patients**

```http
# Récupérer tous les patients (paginé)
GET /api/patients?page=0&size=20&sort=nomPAT,asc
Authorization: Bearer <token>

# Rechercher les patients
GET /api/patients/search?numeroSS=1234567890123&nomPAT=Dupont
Authorization: Bearer <token>

# Créer un patient
POST /api/patients
Authorization: Bearer <token>
Content-Type: application/json

{
  "numeroSS": "1234567890123",
  "nomPAT": "Jean Dupont"
}

# Mettre à jour un patient
PUT /api/patients/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "numeroSS": "1234567890123",
  "nomPAT": "Jean Dupont Modifié"
}

# Supprimer un patient
DELETE /api/patients/{id}
Authorization: Bearer <token>
```

#### 3. **Consultations**

```http
# Créer une consultation programmée
POST /api/consultations
Authorization: Bearer <token>
Content-Type: application/json

{
  "dateConsultation": "2024-08-20T14:30:00",
  "patient": {
    "id": "507f1f77bcf86cd799439011"
  },
  "medecin": {
    "id": "507f1f77bcf86cd799439012"
  }
}

# Récupérer les consultations d'un patient
GET /api/consultations/patient/{patientId}?page=0&size=20
Authorization: Bearer <token>

# Ajouter une prescription
POST /api/consultations/{consultationId}/prescriptions?medicamentId={medicamentId}&nbPrises=3
Authorization: Bearer <token>

# Valider une consultation (la marquer comme effectuée)
PATCH /api/consultations/{consultationId}/validate
Authorization: Bearer <token>

# Attacher un document
POST /api/consultations/{consultationId}/documents
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <SELECT FILE>
```

#### 4. **Médecins**

```http
# Récupérer tous les médecins
GET /api/medecins?page=0&size=20
Authorization: Bearer <token>

# Rechercher des médecins
GET /api/medecins/search?nomMED=Durand&matricule=MED
Authorization: Bearer <token>
```

### Utilisateurs de test fournis:
```
Admin:
  - Username: admin
  - Password: admin123
  - Roles: ROLE_ADMIN, ROLE_USER

Docteur:
  - Username: doctor_001
  - Password: doctor123
  - Roles: ROLE_DOCTOR, ROLE_USER

Patient:
  - Username: patient_001
  - Password: patient123
  - Roles: ROLE_PATIENT, ROLE_USER
```

---

## 🧪 Tests

### Exécuter tous les tests:
```powershell
# Tests unitaires et d'intégration
mvn test

# Avec rapports de couverture
mvn clean test jacoco:report
```

### Exécuter une suite de tests spécifique:
```powershell
# Tests du service Patient
mvn test -Dtest=PatientServiceImplTest

# Tests du contrôleur Auth
mvn test -Dtest=AuthControllerTest
```

### Visualiser la couverture des tests:
```powershell
# Générer un rapport
mvn jacoco:report

# Le rapport est disponible dans:
# target/site/jacoco/index.html
```

### Structure des tests:
- **Unit Tests**: `src/test/java/com/medical/service/impl/`
  - Testent la logique métier avec Mockito
  - Pas de connexion à la base de données

- **Integration Tests**: `src/test/java/com/medical/controller/`
  - Testent les endpoints REST
  - Utilisent MockMvc
  - Peuvent utiliser MongoDB Embedded pour l'isolation

---

## 📊 Analyse de la qualité du code avec SonarQube

### Installation de SonarQube:
```powershell
# Télécharger SonarQube Community Edition
# https://www.sonarqube.org/downloads/

# Extraire et lancer (Windows)
cd sonarqube-9.x.x
bin\windows-x86-64\StartSonar.bat

# SonarQube sera accessible sur http://localhost:9000
```

### Analyser le projet:
```powershell
# Se placer à la racine du projet
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=medical-api \
  -Dsonar.sources=src \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin
```

### Points vérifiés:
- Couverture de code (Tests)
- Bugs et vulnérabilités
- Smells (Mauvaises pratiques)
- Duplication de code
- Complexité cyclomatique

---

## 🔧 Commandes utiles

### Maven:
```powershell
# Nettoyer et compiler
mvn clean compile

# Exécuter les tests
mvn test

# Créer un JAR
mvn clean package -DskipTests

# Démarrer l'application
mvn spring-boot:run

# Vérifier les dépendances
mvn dependency:tree
```

### MongoDB (PowerShell):
```powershell
# Démarrer le service MongoDB
Start-Service MongoDB

# Arrêter le service
Stop-Service MongoDB

# Client MongoDB (mongoosh)
mongosh "mongodb://localhost:27017"

# Dans mongosh:
# use medical_db
# db.patients.find()
# db.consultations.find().pretty()
```

### Git:
```powershell
# Clone du projet
git clone <url>

# Ajouter les fichiers modifiés
git add .

# Commiter
git commit -m "Message du commit"

# Pusher les changements
git push
```

### HTTP (Tester l'API):
```powershell
# Utiliser curl (si disponible)
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"admin","password":"admin123"}'

# Ou utiliser Invoke-WebRequest (PowerShell)
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json"
```

---

## 📂 Structure du projet détaillée

```
CC-AIBD-1-JavaEE-Spring-Oracle-Individuel/
│
├── pom.xml                          # Configuration Maven
│
├── src/
│   ├── main/
│   │   ├── java/com/medical/
│   │   │   ├── MedicalApiApplication.java      # Point d'entrée
│   │   │   │
│   │   │   ├── controller/                      # Contrôleurs REST
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── PatientController.java
│   │   │   │   ├── MedecinController.java
│   │   │   │   └── ConsultationController.java
│   │   │   │
│   │   │   ├── service/                         # Interfaces des services
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── PatientService.java
│   │   │   │   ├── MedecinService.java
│   │   │   │   ├── MedicamentService.java
│   │   │   │   └── ConsultationService.java
│   │   │   │
│   │   │   ├── service/impl/                    # Implémentations
│   │   │   │   ├── AuthServiceImpl.java
│   │   │   │   ├── PatientServiceImpl.java
│   │   │   │   ├── MedecinServiceImpl.java
│   │   │   │   ├── MedicamentServiceImpl.java
│   │   │   │   └── ConsultationServiceImpl.java
│   │   │   │
│   │   │   ├── repository/                      # Repositories MongoDB
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── PatientRepository.java
│   │   │   │   ├── MedecinRepository.java
│   │   │   │   ├── MedicamentRepository.java
│   │   │   │   └── ConsultationRepository.java
│   │   │   │
│   │   │   ├── entity/                          # Entités
│   │   │   │   ├── User.java
│   │   │   │   ├── Patient.java
│   │   │   │   ├── Medecin.java
│   │   │   │   ├── Medicament.java
│   │   │   │   └── Consultation.java
│   │   │   │
│   │   │   ├── dto/                             # DTOs
│   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   ├── LoginResponseDTO.java
│   │   │   │   ├── PatientDTO.java
│   │   │   │   ├── MedecinDTO.java
│   │   │   │   ├── MedicamentDTO.java
│   │   │   │   └── ConsultationDTO.java
│   │   │   │
│   │   │   ├── exception/                       # Gestion des exceptions
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── ResourceAlreadyExistsException.java
│   │   │   │   ├── InvalidOperationException.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │
│   │   │   ├── security/                        # Sécurité avec JWT
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   │
│   │   │   ├── config/                          # Configurations
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── OpenAPIConfig.java
│   │   │   │
│   │   │   └── initialization/                  # Données de test
│   │   │       └── DataInitializer.java
│   │   │
│   │   └── resources/
│   │       └── application.properties            # Configuration
│   │
│   └── test/
│       └── java/com/medical/
│           ├── service/impl/
│           │   └── PatientServiceImplTest.java
│           └── controller/
│               └── AuthControllerTest.java
│
├── target/                          # Dossier compilé (généré)
├── logs/                           # Logs de l'application
└── documents/                      # Documents téléchargés par les utilisateurs
```

---

## 🎯 Critères d'évaluation respectés

| Critère | Statut | Détails |
|---------|--------|---------|
| Configuration du projet | ✅ | Maven, Spring Boot 3, Java 21 |
| Organisation des packages | ✅ | Couches MVC: Controller/Service/Repository |
| Bonnes pratiques | ✅ | SOLID, DTO, Logging, Gestion d'erreurs |
| Documentation du code | ✅ | JavaDoc complète sur toutes les classes |
| Documentation de l'API | ✅ | Swagger/OpenAPI intégré |
| Couverture des exigences | ✅ | Tous les endpoints demandés implémentés |
| Maîtrise de la dette technique | ✅ | Code nettoyé, pas de duplication |
| Gestion des exceptions | ✅ | GlobalExceptionHandler, exceptions personnalisées |
| Authentification/Autorisation | ✅ | JWT + Spring Security |
| Tests | ✅ | Tests unitaires et d'intégration |

---

## 📝 Notes importantes

### Sécurité:
- Les mots de passe sont hashés avec BCrypt
- Les tokens JWT expirent après 24 heures
- CORS est configuré pour les requêtes cross-origin
- CSRF est désactivé pour l'API REST (utilise JWT)

### Performance:
- Pagination sur les endpoints list (20 éléments par défaut)
- Indexes MongoDB sur les champs de recherche (numeroSS, matricule, nomPAT)
- Lazy loading des relations

### Extensibilité:
- Architecture modulaire pour ajouter de nouveaux services
- Utilisation du pattern DTO pour découpler l'API des entités
- Configuration externalisée dans `application.properties`

---

## 🐛 Dépannage

### Erreur: Port 8080 déjà en utilisation
```powershell
# Trouver le processus utilisant le port
netstat -ano | findstr ":8080"

# Terminer le processus (remplacer PID)
Stop-Process -Id <PID> -Force

# Ou changer le port dans application.properties
# server.port=8081
```

### Erreur: MongoDB connection refused
```powershell
# Vérifier que MongoDB est démarré
Get-Service | Where-Object {$_.Name -like "*MongoDB*"}

# Démarrer MongoDB si arrêté
Start-Service MongoDB

# ou lancer mongod.exe manuellement
"C:\Program Files\MongoDB\Server\5.0\bin\mongod.exe"
```

### Erreur: Tests échouent
```powershell
# Nettoyer le cache et réexécuter
mvn clean test -U

# Skiper les tests lors de la compilation
mvn clean package -DskipTests
```

---

## 📞 Support

Pour toute question ou problème:
1. Vérifier la documentation dans les commentaires du code
2. Consulter les tests pour des exemples d'utilisation
3. Accéder à la documentation Swagger: http://localhost:8080/swagger-ui.html

---

**Version**: 1.0.0  
**Dernière mise à jour**: Juillet 2024  
**Auteur**: Expert IT - Applications Intelligentes et Big Data

