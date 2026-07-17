# Architecture et Implémentation - Medical API

## 📐 Vue d'ensemble de l'architecture

### Diagramme du flux de requête:

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT (Navigateur/Postman)                  │
└────────────────────────┬──────────────────────────────────────────────┘
                         │ HTTP Request
                         ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SPRING SECURITY (Sécurité)                        │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │  1. JwtAuthenticationFilter - Extrait & valide le token JWT │    │
│  │  2. SecurityContextHolder - Stocke l'authentification       │    │
│  │  3. @PreAuthorize - Vérifie les permissions               │    │
│  └─────────────────────────────────────────────────────────────┘    │
└──────────────────────────┬──────────────────────────────────────────┘
                           │ Requête authentifiée
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     REST CONTROLLERS                                 │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │ • AuthController - Authentification (Login)                 │    │
│  │ • PatientController - Gestion des patients                  │    │
│  │ • MedecinController - Gestion des médecins                  │    │
│  │ • ConsultationController - Gestion des consultations        │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                       │
│  Responsabilités:                                                   │
│  - Valide les requêtes HTTP                                         │
│  - Appelle les services                                             │
│  - Retourne les réponses JSON                                       │
└──────────────────────────┬──────────────────────────────────────────┘
                           │ Appel métier
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER (Logique métier)                  │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │ • Validation des données métier                             │    │
│  │ • Gestion des erreurs (exceptions)                          │    │
│  │ • Transactions (ACID)                                       │    │
│  │ • Logging et traçabilité                                    │    │
│  │ • Conversion DTO <-> Entité                                 │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                       │
│  Services:                                                          │
│  - AuthService - Authentification et gestion JWT                    │
│  - PatientService - Logique Patient (CRUD + Recherche)             │
│  - MedecinService - Logique Médecin (CRUD + Recherche)             │
│  - MedicamentService - Logique Médicament (CRUD)                   │
│  - ConsultationService - Logique Consultation (CRUD + Prescriptions│
└──────────────────────────┬──────────────────────────────────────────┘
                           │ Accès aux données
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                   DATA ACCESS LAYER (Repositories)                   │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │ • MongoRepository - Opérations CRUD                         │    │
│  │ • Requêtes personnalisées (find, search)                    │    │
│  │ • Pagination et tri                                         │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                       │
│  Repositories:                                                      │
│  - UserRepository                                                   │
│  - PatientRepository                                                │
│  - MedecinRepository                                                │
│  - MedicamentRepository                                             │
│  - ConsultationRepository                                           │
└──────────────────────────┬──────────────────────────────────────────┘
                           │ Requête MongoDB
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    MONGODB (Base de données)                         │
│  ┌──────────┬──────────┬──────────┬──────────┬──────────┐           │
│  │ patients │ medecins │consultations       │ users │   │           │
│  └──────────┴──────────┴──────────┴──────────┴──────────┘           │
│                                                                       │
│  Collections:                                                       │
│  - patients (Numérique/Pagination)                                  │
│  - medecins (Matricule unique)                                      │
│  - consultations (Références à patients et medecins)                │
│  - medicaments (Code unique)                                        │
│  - users (Username unique, mot de passe BCrypt)                     │
└──────────────────────────┬──────────────────────────────────────────┘
                           │ Données trouvées
                           ▼ (chemin inversé)
                     RÉPONSE JSON
                  ┌─────────────────┐
                  │ { data: [...] } │
                  └─────────────────┘
```

---

## 🔐 Flux d'authentification JWT

```
1. LOGIN (POST /api/auth/login)
   ├─ Reçoit: { username, password }
   ├─ Valide les identifiants (BCrypt)
   ├─ Génère un token JWT signé
   └─ Retourne: { token, username, roles, ... }

2. UTILISATION DU TOKEN
   ├─ Client stocke le token
   └─ Envoie dans chaque requête: Authorization: Bearer <token>

3. VALIDATION DU TOKEN (JwtAuthenticationFilter)
   ├─ Extrait le token du header Authorization
   ├─ Valide la signature et l'expiration
   ├─ Charge les autorités de l'utilisateur
   └─ Crée une Authentication dans le SecurityContext

4. VÉRIFICATION DES PERMISSIONS (@PreAuthorize)
   ├─ Vérifie les rôles (ROLE_ADMIN, ROLE_DOCTOR, etc.)
   ├─ Autorise ou refuse l'accès
   └─ Retourne: 403 Forbidden si non autorisé
```

### Structure d'un JWT:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
  eyJzdWIiOiJkb2N0b3JfMDAxIiwicm9sZXMiOlsiUk9MRV9ET0NUT1IiXSwiZXhwIjoxNjI0OTk5OTk5fQ.
  signature...

Header (Base64):
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload (Base64):
{
  "sub": "doctor_001",        # Username
  "roles": ["ROLE_DOCTOR"],   # Autorités
  "iat": 1624999999,          # Timestamp création
  "exp": 1625086399           # Timestamp expiration
}

Signature: HMACSHA256(header.payload, secret_key)
```

---

## 🗂️ Patterns et Bonnes pratiques utilisées

### 1. **Design Pattern: Dependency Injection**
```java
@Service
public class PatientServiceImpl {
    private final PatientRepository repository;
    
    // Injection via constructeur (Lombok @RequiredArgsConstructor)
    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }
}
```
**Avantages**: Testabilité (facilite le mocking), Loose coupling, Réutilisabilité

### 2. **Design Pattern: DTO (Data Transfer Object)**
```java
// Entité (Persistance)
@Document
public class Patient {
    private String id;
    private String numeroSS;
    private String nomPAT;
}

// DTO (Transport)
@Data
public class PatientDTO {
    private String id;
    private String numeroSS;
    private String nomPAT;
}
```
**Avantages**: Sécurité (ne pas exposer tous les champs), Découpling API/BDD, Transformation flexible

### 3. **Design Pattern: Repository**
```java
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByNumeroSS(String numeroSS);
    Page<Patient> findByNomPATContainingIgnoreCase(String nom, Pageable page);
}
```
**Avantages**: Abstraction de la persistance, Réutilisabilité, Testabilité

### 4. **Gestion des exceptions: Exception Hierarchy**
```
Exception
├── ResourceNotFoundException      (404 - Ressource non trouvée)
├── ResourceAlreadyExistsException (409 - Ressource existe déjà)
└── InvalidOperationException      (400 - Opération invalide)
```
**Avantage**: Gestion fine des erreurs avec codes HTTP appropriés

### 5. **Logging: Structured Logging**
```java
@Slf4j
public class PatientService {
    public void createPatient(PatientDTO dto) {
        log.info("Création d'un patient - NumeroSS: {}", dto.getNumeroSS());
        // ...
        log.debug("Patient créé - ID: {}", patient.getId());
    }
}
```
**Avantages**: Traçabilité, Debugging, Monitoring, Analyse

### 6. **Transactions ACID**
```java
@Transactional
public void deletePatient(String id) {
    // Si une erreur occure, tout est rollback
    // Garantit la cohérence des données
}
```

### 7. **Pagination et Tri**
```java
Page<PatientDTO> getAllPatients(Pageable pageable);

// Utilisation:
// GET /api/patients?page=0&size=20&sort=nomPAT,asc
```

### 8. **Lombock - Réduction de verbosité**
```java
@Data              // @Getter @Setter @ToString @EqualsAndHashCode
@NoArgsConstructor // Constructeur vide
@AllArgsConstructor // Constructeur avec tous les champs
@Builder           // Pattern Builder
@Slf4j             // Logger
public class Patient { ... }
```

### 9. **OpenAPI/Swagger - Documentation**
```java
@Operation(
    summary = "Récupère tous les patients",
    responses = {
        @ApiResponse(responseCode = "200", description = "Patients récupérés"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    }
)
public ResponseEntity<Page<PatientDTO>> getAllPatients(Pageable pageable) {...}
```

### 10. **CORS - Cross-Origin Resource Sharing**
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    // ...
}
```

---

## 📊 Modèle de données MongoDB

### Collections:

#### 1. **patients**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "numeroSS": "1234567890123",
  "nomPAT": "Jean Dupont"
}
```
**Index**: { numeroSS: 1 } (unique)

#### 2. **medecins**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439012"),
  "matricule": "MED001",
  "nomMED": "Dr. Martin Durand"
}
```
**Index**: { matricule: 1 } (unique)

#### 3. **medicaments**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439013"),
  "code": "AMOXICILLINE",
  "libelle": "Amoxicilline 500mg comprimé"
}
```
**Index**: { code: 1 } (unique)

#### 4. **consultations**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439014"),
  "numero": "CONS1624999999",
  "dateConsultation": ISODate("2024-08-20T14:30:00Z"),
  "patient": ObjectId("507f1f77bcf86cd799439011"),
  "medecin": ObjectId("507f1f77bcf86cd799439012"),
  "medicaments": [
    {
      "medicament": ObjectId("507f1f77bcf86cd799439013"),
      "nbPrises": 3
    }
  ],
  "documentPath": "documents/CONS1624999999_rapport.pdf",
  "statut": "PROGRAMMEE"
}
```
**Index**: { patientId: 1, dateConsultation: -1 } (tri par date)

#### 5. **users**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439015"),
  "username": "doctor_001",
  "password": "$2a$10$N9q...",  // BCrypt hash
  "email": "doctor@medical-api.fr",
  "roles": ["ROLE_DOCTOR", "ROLE_USER"],
  "actif": true
}
```
**Index**: { username: 1 } (unique)

---

## 🔍 Flux détaillé d'une opération CRUD

### Exemple: Créer une consultation

```
1. CLIENT ENVOIE LA REQUÊTE
   POST /api/consultations
   Authorization: Bearer <token>
   {
     "dateConsultation": "2024-08-20T14:30:00",
     "patient": { "id": "507f1f77bcf86cd799439011" },
     "medecin": { "id": "507f1f77bcf86cd799439012" }
   }

2. SPRING SECURITY (JwtAuthenticationFilter)
   ├─ Extrait le token du header Authorization
   ├─ Appelle JwtTokenProvider.validateToken()
   ├─ Charge l'utilisateur avec authService.loadUserByUsername()
   ├─ Crée une Authentication
   └─ Stocke dans SecurityContextHolder

3. ROUTING
   ├─ Spring mapping la requête à ConsultationController.createConsultation()
   └─ Déserialize le JSON en ConsultationDTO

4. CONTRÔLEUR (ConsultationController)
   ├─ Valide les paramètres HTTP
   ├─ Appelle consultationService.createConsultation(dto)
   └─ Retourne ResponseEntity avec le DTO créé

5. SERVICE (ConsultationServiceImpl)
   ├─ Valide les règles métier:
   │  ├─ La date doit être dans le futur
   │  └─ Patient et médecin doivent exister
   ├─ Récupère le Patient et Médecin du repository
   ├─ Génère un numéro unique
   ├─ Crée l'entité Consultation
   ├─ Appelle consultationRepository.save()
   ├─ Convertit en DTO
   └─ Retourne le DTO créé

6. REPOSITORY (ConsultationRepository)
   ├─ Appelle save() qui:
   │  ├─ Insère le document dans MongoDB
   │  ├─ Génère l'_id (ObjectId)
   │  └─ Retourne l'entité persistée

7. MONGODB
   ├─ Insère le document dans la collection 'consultations'
   ├─ Retourne le document avec l'_id
   └─ Index sur patientId et dateConsultation

8. RÉPONSE AU CLIENT
   HTTP 201 Created
   {
     "id": "507f1f77bcf86cd799439014",
     "numero": "CONS1624999999",
     "dateConsultation": "2024-08-20T14:30:00",
     "patient": { ... },
     "medecin": { ... },
     "medicaments": [],
     "statut": "PROGRAMMEE"
   }

9. ERREURS POSSIBLES
   ├─ 400 Bad Request: Date dans le passé
   ├─ 404 Not Found: Patient ou médecin n'existe pas
   ├─ 401 Unauthorized: Token invalide ou expiré
   └─ 500 Internal Server Error: Erreur base de données
```

---

## 🧪 Stratégie de test

### Test Pyramid:
```
        ▲ Unit Tests (80%)         Rapides, isolés, nombreux
       ╱ ╲ Service/Repository tests
      ╱   ╲ Integration Tests (15%) Testent plusieurs couches
     ╱     ╲ Controller tests
    ╱───────╲
   ╱         ╲ End-to-End Tests (5%) Complets mais lents
  ╱           ╲
 ╱_____________╲
```

### Types de tests fournis:

1. **Unit Tests (Services)**
   ```java
   @ExtendWith(MockitoExtension.class)
   class PatientServiceImplTest {
       @Mock PatientRepository repository;
       @InjectMocks PatientServiceImpl service;
       
       @Test
       void testCreatePatient_Success() { ... }
   }
   ```

2. **Integration Tests (Controllers)**
   ```java
   @SpringBootTest
   @AutoConfigureMockMvc
   class AuthControllerTest {
       @Autowired MockMvc mockMvc;
       
       @Test
       void testLogin_Success() { ... }
   }
   ```

---

## 📈 Métriques de qualité

### Couverture de code (JaCoCo):
- **Line Coverage**: % de lignes exécutées par les tests
- **Branch Coverage**: % de branches logiques couvertes
- **Method Coverage**: % de méthodes couvertes

### Complexité cyclomatique:
- Nombre de chemins d'exécution différents
- Objectif: < 10 par méthode

### Duplication de code:
- Utiliser des abstractions (services, utilitaires)
- Objectif: < 5% du code dupliqué

### Bugs potentiels:
- Vérifiés avec SpotBugs
- Objectif: 0 bugs critiques

---

## 🚀 Points de performance

1. **Pagination**: Évite de charger tous les données
2. **Index MongoDB**: Sur numeroSS, matricule, patientId
3. **Lazy Loading**: Les relations ne sont chargées que si nécessaire
4. **Caching**: Peut être ajouté pour les données fréquemment accédées
5. **Connection Pooling**: MongoDB gère automatiquement le pool

---

## 🔒 Points de sécurité

1. **Password Hashing**: BCrypt avec salt automatique
2. **JWT Token**: Expirant après 24 heures
3. **CORS**: Restreint aux origines autorisées
4. **SQL Injection**: N/A (MongoDB ne utilise pas SQL, mais attention à l'injection NoSQL)
5. **CSRF**: Désactivé pour REST API au profit des tokens JWT
6. **Validations Input**: @Valid sur les DTOs
7. **Roles & Permissions**: Spring Security @PreAuthorize

---

Dernière mise à jour: Juillet 2024

