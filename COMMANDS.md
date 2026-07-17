# Scripts de démarrage et commandes pour Medical API

## 📝 Table des matières
1. [Démarrage de l'application](#démarrage-de-lapplication)
2. [Exécution des tests](#exécution-des-tests)
3. [Analyse de qualité](#analyse-de-qualité)
4. [Gestion de MongoDB](#gestion-de-mongodb)
5. [Requêtes HTTP d'exemple](#requêtes-http-dexemple)

---

## ▶️ Démarrage de l'application

### Script PowerShell pour Windows

#### 1. Démarrage simple avec Maven:
```powershell
# Ouvrir PowerShell ou Terminal dans le dossier du projet
cd C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel

# Démarrer l'application
mvn spring-boot:run

# L'application démarre sur http://localhost:8080
# Logs affichés en temps réel
```

#### 2. Démarrage avec build (JAR):
```powershell
# Compiler le projet en JAR
mvn clean package -DskipTests

# Exécuter le JAR généré
java -jar target/medical-api-1.0.0.jar

# Le JAR sera dans: target/medical-api-1.0.0.jar
```

#### 3. Arrêter l'application:
```powershell
# Depuis le terminal où Maven/Java est en cours d'exécution
# Appuyer sur: Ctrl + C

# Ou depuis un autre terminal - trouver et terminer le processus
Get-Process java | Stop-Process -Force
```

### Script Bash (Linux/macOS):

```bash
#!/bin/bash

# Démarrer l'application
cd ~/path/to/project
mvn spring-boot:run

# Ou avec le JAR
java -jar target/medical-api-1.0.0.jar
```

---

## 🧪 Exécution des tests

### Exécuter tous les tests:
```powershell
mvn test
```

### Exécuter une suite de tests spécifique:
```powershell
# Tests du service Patient
mvn test -Dtest=PatientServiceImplTest

# Tests du contrôleur Auth
mvn test -Dtest=AuthControllerTest

# Tous les tests avec pattern
mvn test -Dtest=*Service*Test
```

### Exécuter les tests avec rapport de couverture:
```powershell
# Générer un rapport JaCoCo
mvn clean test jacoco:report

# Le rapport est créé dans: target/site/jacoco/index.html
# Ouvrir dans un navigateur
Start-Process target/site/jacoco/index.html
```

### Exécuter les tests sans compiler à nouveau:
```powershell
mvn test -o
```

### Exécuter un test spécifique avec verbose:
```powershell
mvn test -Dtest=PatientServiceImplTest#testCreatePatient_Success -X
```

---

## 📊 Analyse de qualité

### Avec Maven Compiler:
```powershell
# Vérifier les warnings et erreurs de compilation
mvn clean compile
```

### Avec SonarQube (Qualité du code):
```powershell
# 1. Démarrer SonarQube (si installé)
# C:\sonarqube\bin\windows-x86-64\StartSonar.bat

# 2. Analyser le projet
mvn clean verify sonar:sonar `
  -Dsonar.projectKey=medical-api `
  -Dsonar.sources=src `
  -Dsonar.host.url=http://localhost:9000 `
  -Dsonar.login=admin `
  -Dsonar.password=admin

# 3. Consulter le rapport sur http://localhost:9000
Start-Process http://localhost:9000
```

### Avec SpotBugs (Détection de bugs):
```powershell
mvn clean compile spotbugs:check
```

---

## 🗄️ Gestion de MongoDB

### Démarrer/Arrêter MongoDB (Windows Service):
```powershell
# Vérifier le statut
Get-Service MongoDB

# Démarrer le service
Start-Service MongoDB

# Arrêter le service
Stop-Service MongoDB

# Redémarrer le service
Restart-Service MongoDB
```

### Accéder à MongoDB Shell:
```powershell
# Lancer mongosh (client MongoDB nouveau)
mongosh "mongodb://localhost:27017"

# Commandes utiles dans mongosh:
# use medical_db                          # Sélectionner la base
# db.patients.find()                      # Lister tous les patients
# db.patients.find({nomPAT: "Dupont"})   # Rechercher par nom
# db.consultations.find().pretty()        # Lister les consultations
# db.patients.insertOne({numeroSS: "1234567890123", nomPAT: "Test"})
# db.patients.deleteMany({nomPAT: "Test"})
# db.patients.countDocuments()            # Compter les documents
# exit                                     # Quitter
```

### Sauvegarder/Restaurer la base:
```powershell
# Exporter les données
mongodump --uri "mongodb://localhost:27017" --out ./backup

# Importer les données
mongorestore ./backup

# Exporter une collection en JSON
mongoexport --collection=patients --db=medical_db -o patients.json

# Importer depuis JSON
mongoimport --collection=patients --db=medical_db --file patients.json
```

### Afficher les informations:
```powershell
mongosh "mongodb://localhost:27017"

# Dans mongosh:
db.version()                            # Version MongoDB
db.stats()                              # Statistiques de la base
db.getCollectionNames()                 # Lister les collections
db.patients.stats()                     # Statistiques d'une collection
```

---

## 🌐 Requêtes HTTP d'exemple

### Utiliser curl (PowerShell):
```powershell
# Fonction helper pour les requêtes
function Test-API {
    param(
        [string]$Method = "GET",
        [string]$Uri = "http://localhost:8080",
        [string]$Token,
        [object]$Body
    )

    $headers = @{
        "Content-Type" = "application/json"
    }

    if ($Token) {
        $headers["Authorization"] = "Bearer $Token"
    }

    $params = @{
        Method = $Method
        Uri = $Uri
        Headers = $headers
    }

    if ($Body) {
        $params["Body"] = ($Body | ConvertTo-Json)
    }

    Invoke-WebRequest @params
}

# Utilisation:
# Test-API -Method POST -Uri "http://localhost:8080/api/auth/login" -Body @{username="admin"; password="admin123"}
```

### 1. Authentification (Connexion):
```powershell
# Login
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $loginBody

$token = ($response.Content | ConvertFrom-Json).token

Write-Host "Token reçu: $token"
```

### 2. Récupérer les patients:
```powershell
$headers = @{
    Authorization = "Bearer $token"
}

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/patients?page=0&size=20" `
    -Method GET `
    -Headers $headers

$response.Content | ConvertFrom-Json | Format-Table
```

### 3. Rechercher des patients:
```powershell
$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/patients/search?nomPAT=Dupont" `
    -Method GET `
    -Headers $headers

$response.Content | ConvertFrom-Json
```

### 4. Créer un patient:
```powershell
$createBody = @{
    numeroSS = "5555555555555"
    nomPAT = "Nouveau Patient"
} | ConvertTo-Json

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/patients" `
    -Method POST `
    -ContentType "application/json" `
    -Headers $headers `
    -Body $createBody

$createdPatient = $response.Content | ConvertFrom-Json
Write-Host "Patient créé avec l'ID: $($createdPatient.id)"
```

### 5. Créer une consultation:
```powershell
$consultationBody = @{
    dateConsultation = "2024-08-20T14:30:00"
    patient = @{ id = "507f1f77bcf86cd799439011" }
    medecin = @{ id = "507f1f77bcf86cd799439012" }
} | ConvertTo-Json

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/consultations" `
    -Method POST `
    -ContentType "application/json" `
    -Headers $headers `
    -Body $consultationBody

$response.Content | ConvertFrom-Json
```

### 6. Ajouter une prescription:
```powershell
$consultationId = "507f1f77bcf86cd799439013"
$medicamentId = "507f1f77bcf86cd799439014"
$nbPrises = 3

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/consultations/$consultationId/prescriptions?medicamentId=$medicamentId&nbPrises=$nbPrises" `
    -Method POST `
    -Headers $headers

$response.Content | ConvertFrom-Json
```

### 7. Attacher un document:
```powershell
$consultationId = "507f1f77bcf86cd799439013"
$filePath = "C:\path\to\document.pdf"

$response = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/consultations/$consultationId/documents" `
    -Method POST `
    -Headers $headers `
    -Form @{ file = Get-Item $filePath }

$response.Content | ConvertFrom-Json
```

---

## 🔍 Commandes de diagnostic

### Vérifier que l'application est prête:
```powershell
# Attendre que l'application soit prête (max 30 secondes)
$ready = $false
$counter = 0
while (-not $ready -and $counter -lt 30) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui.html" -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            $ready = $true
            Write-Host "✅ Application prête!"
        }
    } catch {
        $counter++
        Write-Host "⏳ En attente... ($counter/30)"
        Start-Sleep -Seconds 1
    }
}
```

### Logs de l'application:
```powershell
# Afficher les logs en temps réel
Get-Content -Path logs/medical-api.log -Tail 100 -Wait

# Rechercher une erreur dans les logs
Select-String -Path logs/medical-api.log -Pattern "ERROR"

# Compter les avertissements
(Select-String -Path logs/medical-api.log -Pattern "WARN" | Measure-Object).Count
```

### Vérifier les connexions réseau:
```powershell
# Vérifier le port 8080
netstat -ano | findstr ":8080"

# Vérifier la connexion MongoDB
netstat -ano | findstr ":27017"

# Tester la connectivité
Test-NetConnection localhost -Port 8080
Test-NetConnection localhost -Port 27017
```

---

## 📋 Checklist de mise en place

```powershell
# 1. Vérifier les prérequis
java -version
mvn -version
mongod --version

# 2. Cloner/Télécharger le projet
# ✅ Projet dans le dossier

# 3. Télécharger les dépendances
mvn clean install

# 4. Vérifier MongoDB
Start-Service MongoDB
mongosh

# 5. Tester la compilation
mvn compile

# 6. Exécuter les tests
mvn test

# 7. Démarrer l'application
mvn spring-boot:run

# 8. Vérifier l'application
# Ouvrir http://localhost:8080/swagger-ui.html dans le navigateur

# ✅ Prêt à l'usage!
```

---

## 🚑 Troubleshooting rapide

```powershell
# Problème: Port déjà utilisé
# Solution:
netstat -ano | findstr ":8080"
Stop-Process -Id <PID> -Force

# Problème: MongoDB non accessible
# Solution:
Start-Service MongoDB

# Problème: Tests échouent
# Solution:
mvn clean test

# Problème: Dépendances manquantes
# Solution:
mvn clean install -U

# Problème: JAR ne peut pas être créé
# Solution:
mvn clean package -DskipTests
```

---

**Dernière mise à jour**: Juillet 2024

