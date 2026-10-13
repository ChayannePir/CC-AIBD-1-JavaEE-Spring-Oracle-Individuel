# COMMANDS.md

Ce fichier regroupe les commandes utiles pour lancer, tester et vérifier le projet Medical API.

## 1. Vérifier l'environnement

```powershell
java -version
mvn -version
```

## 2. Nettoyer le projet

```powershell
mvn clean
```

## 3. Compiler le projet

```powershell
mvn compile
```

## 4. Installer les dépendances et générer le package

```powershell
mvn clean install
```

## 5. Lancer l'application

```powershell
cd C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel
mvn spring-boot:run
```

## 6. Lancer l'application en arrière-plan

```powershell
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel'; mvn spring-boot:run"
```

## 7. Démarrer avec un profil spécifique

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 8. Exécuter les tests

```powershell
mvn test
```

## 9. Exécuter un test précis

```powershell
mvn test -Dtest=AuthControllerTest
```

## 10. Vérifier le packaging

```powershell
mvn package
```

## 11. Vérifier la documentation Swagger

Une fois l'application lancée :

```text
http://localhost:8080/swagger-ui.html
```

## 12. Tester l'API avec curl

### Connexion / login

```powershell
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"doctor_001\",\"password\":\"doctor123\"}"
```

### Appel d'un endpoint protégé

```powershell
curl -X GET "http://localhost:8080/api/patients" -H "Authorization: Bearer <TOKEN>"
```

## 13. Vérifier MongoDB

### Démarrer MongoDB localement

```powershell
mongod
```

### Vérifier la base

```powershell
mongosh "mongodb://localhost:27017/medical_db"
```

## 14. Commandes de secours

### Nettoyer complètement le projet

```powershell
Remove-Item -Recurse -Force target
mvn clean install
```

### Télécharger les dépendances forcées

```powershell
mvn -U clean install
```

## 15. Résumé rapide

```powershell
mvn clean install
mvn spring-boot:run
mvn test
```

## 16. Vérification du bon démarrage

Le projet est correctement démarré si la console affiche :

```text
Tomcat started on port 8080
Started MedicalApiApplication
```

## 17. Points utiles

- Application : http://localhost:8080
- Swagger : http://localhost:8080/swagger-ui.html
- MongoDB : mongodb://localhost:27017/medical_db
