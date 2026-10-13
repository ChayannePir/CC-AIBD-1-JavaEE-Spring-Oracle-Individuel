### 1. Lancement de l'API

* **Action :** Lance l'application depuis PowerShell (`mvn spring-boot:run`).

> **À l'oral :** « Bonjour à tous. Je lance l'application Spring Boot. Au démarrage, elle initialise les comptes de démonstration et ouvre le serveur web sur le port 8080. »

---

### 2. Ouverture de Swagger UI

* **Action :** Ouvre le navigateur sur `http://localhost:8080/swagger-ui/index.html`.

> **À l'oral :** « Je me rends sur l'interface Swagger UI. Elle centralise la documentation interactive de notre API Medical et liste tous les contrôleurs disponibles. »

---

### 3. Connexion et génération du JWT

* **Action :** Ouvre `POST /api/auth/login` $\rightarrow$ **Try it out** $\rightarrow$ colle les identifiants médecin $\rightarrow$ **Execute** $\rightarrow$ copie la valeur du `token`.

```json
{
  "username": "doctor_001",
  "password": "doctor123"
}

```

> **À l'oral :** « Je commence par m'authentifier sur `/api/auth/login` avec le profil médecin `doctor_001`. L'API valide l'accès et me renvoie un jeton JWT que je copie. »

---

### 4. Activation du token dans Swagger

* **Action :** Clique sur le bouton **Authorize** (haut à droite) $\rightarrow$ colle `Bearer <votre_token>` $\rightarrow$ **Authorize** $\rightarrow$ **Close**.

> **À l'oral :** « Pour sécuriser l'interface, je clique sur le bouton *Authorize* en haut à droite. Saisissez `Bearer` suivi du jeton. Désormais, toutes les requêtes envoyées par Swagger incluront le header HTTP `Authorization`. »

---

### 5. Consultation des données protégées

* **Action :** Ouvre `GET /api/patients` $\rightarrow$ **Try it out** $\rightarrow$ **Execute**.

> **À l'oral :** « Je peux maintenant interroger les endpoints restreints. J'exécute une requête `GET` sur `/api/patients` : l'API me retourne un code HTTP 200 et le flux JSON des patients. »

---

### 6. Test d'upload de document *(facultatif)*

* **Action :** Ouvre `POST /api/consultations/{consultationId}/documents` $\rightarrow$ **Try it out** $\rightarrow$ `consultationId` : `1` $\rightarrow$ choisis un fichier local $\rightarrow$ **Execute**.

> **À l'oral :** « Swagger permet également de tester l'envoi de fichiers en `multipart/form-data`. Je choisis une consultation, je sélectionne une pièce jointe et j'exécute pour confirmer la réception côté serveur. »

---

### 7. Conclusion

> **À l'oral :** « Voilà pour la démonstration. Swagger UI nous permet de valider l'ensemble du cycle de vie de l'API — authentification JWT, droits d'accès et traitement de données — directement depuis le navigateur. »