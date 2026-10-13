# Démo Swagger UI — Medical API

Checklist (rapide)

- [ ] Démarrer MongoDB (si utilisé localement)
- [ ] Lancer l'application Spring Boot
- [ ] Ouvrir Swagger UI
- [ ] Se connecter via /api/auth/login (Swagger)
- [ ] Cliquer sur "Authorize" et coller le JWT (préfixer par "Bearer ")
- [ ] Appeler des endpoints protégés depuis Swagger

But : montrer la manipulation complète uniquement depuis l'interface Swagger UI fournie par l'application.

## 1) Prérequis

- Java 17
- Maven
- MongoDB (si votre configuration l'exige)

Vérification rapide :

```powershell
java -version
mvn -version
```

## 2) Lancer l'application

Depuis le dossier du projet :

```powershell
cd "C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel"
mvn spring-boot:run
```

ou après build :

```powershell
mvn clean package
java -jar target\medical-api-1.0.0.jar
```

L'application web sera disponible sur http://localhost:8080

## 3) Ouvrir Swagger UI

- URL habituelle : http://localhost:8080/swagger-ui.html
- si nécessaire : http://localhost:8080/swagger-ui/index.html

Vous verrez les groupes d'API : `/api/auth`, `/api/patients`, `/api/medecins`, `/api/consultations`, ...

## 4) Identifiants de démonstration (initialisés automatiquement)

- admin / admin123
- doctor_001 / doctor123
- patient_001 / patient123

Ces comptes sont créés par la classe `DataInitializer` au démarrage (vérifiez les logs pour confirmation).

## 5) Obtenir un JWT depuis Swagger

1. Dans Swagger UI, ouvrez `POST /api/auth/login`.
2. Cliquez sur "Try it out".
3. Remplissez le JSON de la requête. Exemple :

```json
{
  "username": "doctor_001",
  "password": "doctor123"
}
```

4. Cliquez sur "Execute".
5. La réponse contient le jeton JWT (ex. champ `token` ou `accessToken`). Copiez la valeur.

Exemple de réponse (attendue) :

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "doctor_001"
}
```

## 6) S'authentifier dans Swagger (Authorize)

1. Cliquez sur le bouton "Authorize" (coin supérieur droit de Swagger UI).
2. Dans le champ de la fenêtre d'authentification, collez exactement :

```
Bearer <votre_token_ici>
```

3. Validez. Toutes les requêtes suivantes envoyées depuis Swagger incluront l'en-tête `Authorization: Bearer <token>`.

Remarque : si l'UI ne préfixe pas automatiquement par `Bearer `, incluez le préfixe vous-même.

## 7) Appeler des endpoints protégés (exemples)

- GET /api/patients — liste paginée des patients
- GET /api/patients/{id} — détail d'un patient
- GET /api/consultations/patient/{patientId} — consultations d'un patient
- GET /api/consultations/{consultationId}/medicaments — médicaments d'une consultation

Procédure :

1. Ouvrez l'opération souhaitée dans Swagger UI.
2. Cliquez sur "Try it out".
3. Remplissez les paramètres si besoin (path, query, body).
4. Cliquez "Execute" et observez la réponse.

Exemple : Lister les patients

1. Ouvrez `GET /api/patients`.
2. Try it out → Execute.
3. Vous devriez voir `200 OK` et un tableau JSON (ou [] si vide).

## 8) Upload de document sur une consultation (directement via Swagger)

Si le contrôleur expose `POST /api/consultations/{consultationId}/documents` acceptant un multipart file :

1. Ouvrez l'opération `POST /api/consultations/{consultationId}/documents`.
2. Click "Try it out".
3. Renseignez `consultationId`.
4. Dans le champ de type file, cliquez sur "Choose File" et sélectionnez votre document local.
5. Executez. La réponse doit confirmer l'upload (nom du fichier, id, URL, ...).

Remarque : si Swagger n'affiche pas le champ file, le contrôleur n'est pas correctement annoté pour multipart.

## 9) Exemple de scénario de démo (en 3–5 minutes)

1. Démarrage rapide : lancer l'application (mvn spring-boot:run).
2. Ouvrir Swagger UI.
3. POST /api/auth/login avec `doctor_001` / `doctor123`.
4. Copier le JWT et cliquer sur "Authorize".
5. GET /api/patients (afficher la réponse).
6. POST /api/consultations/{id}/documents pour montrer l'upload (facultatif).

## 10) Commandes utiles (PowerShell)

```powershell
mvn clean install
mvn test
mvn spring-boot:run
```

Exemples curl (hors Swagger) :

```powershell
# Obtenir le token
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"doctor_001","password":"doctor123"}'

# Appeler l'API protégée
curl -X GET "http://localhost:8080/api/patients" -H "Authorization: Bearer eyJhbGciOi..."
```

## 11) Dépannage rapide

- 401 Unauthorized : le token est incorrect, expiré, ou non fourni. Regénérez un token via `/api/auth/login` et réautorisez Swagger.
- Le bouton "Authorize" n'applique pas le header : collez manuellement `Authorization: Bearer <token>` dans le champ de headers si disponible.
- Port occupé : modifier `server.port` dans `application.properties` ou libérer le port 8080.
- MongoDB inaccessible : vérifiez `spring.data.mongodb.uri` et que `mongod` tourne.

## 12) Vérifications sur l'instance

- Dans les logs au démarrage, `DataInitializer` écrit les comptes créés (admin / doctor_001 / patient_001). Vérifiez la présence de ces lignes si vous avez un doute.
- Swagger doit exposer les modèles DTO (LoginRequestDTO, LoginResponseDTO, PatientDTO, ConsultationDTO...) et les opérations correspondantes.

Fin — Cette procédure vous permet de réaliser toute la démonstration en utilisant uniquement l'interface interactive Swagger UI.
