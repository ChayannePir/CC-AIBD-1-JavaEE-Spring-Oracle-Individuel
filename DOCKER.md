# 🐳 Guide de déploiement avec Docker

## 📋 Prérequis

- **Docker**: Télécharger depuis https://www.docker.com/products/docker-desktop
- **Docker Compose**: Inclus avec Docker Desktop

## 🚀 Démarrage rapide avec Docker

### 1. Démarrer les services (MongoDB + API)
```powershell
# Se placer dans le répertoire du projet
cd C:\Users\Admin Stagiaire\Documents\CC-AIBD-1-JavaEE-Spring-Oracle-Individuel

# Lancer les services en arrière-plan
docker-compose up -d

# Attendre quelques secondes pour que les services démarrent
Start-Sleep -Seconds 10

# Vérifier que les services sont lancés
docker-compose ps
```

### 2. Accéder à l'application
```
Accueil:           http://localhost:8080
Swagger UI:        http://localhost:8080/swagger-ui.html
OpenAPI JSON:      http://localhost:8080/v3/api-docs
Mongo Express:     http://localhost:8081 (admin/admin)
```

### 3. Voir les logs
```powershell
# Logs en temps réel
docker-compose logs -f medical-api

# Logs de MongoDB
docker-compose logs -f mongodb

# Dernières 100 lignes
docker-compose logs --tail=100
```

### 4. Arrêter les services
```powershell
# Arrêter sans supprimer les volumes (données persistent)
docker-compose stop

# Redémarrer
docker-compose start

# Arrêter et supprimer tout
docker-compose down

# Arrêter et supprimer les volumes (supprime aussi les données)
docker-compose down -v
```

## 🔍 Commandes utiles

### Vérifier l'état des conteneurs
```powershell
# Liste des conteneurs Docker
docker ps

# Details détaillés
docker inspect medical-api
docker inspect medical-mongodb

# Logs d'un conteneur spécifique
docker logs medical-api
```

### Exécuter des commandes dans les conteneurs

#### Dans le conteneur API:
```powershell
# Ouvrir un shell dans le conteneur
docker exec -it medical-api /bin/sh

# Vérifier la version Java
docker exec medical-api java -version

# Voir les logs depuis le conteneur
docker exec medical-api tail -f /app/logs/medical-api.log
```

#### Dans le conteneur MongoDB:
```powershell
# Ouvrir le shell MongoDB
docker exec -it medical-mongodb mongosh -u mongouser -p mongopass --authenticationDatabase admin

# Dans mongosh:
use medical_db
db.patients.find()
db.patients.countDocuments()
db.consultations.find().pretty()
```

## 🔧 Configuration

### Modifier les variables d'environnement

Éditer `docker-compose.yml`:
```yaml
environment:
  SPRING_DATA_MONGODB_URI: mongodb://mongouser:mongopass@mongodb:27017/medical_db
  JWT_SECRET: votre-clé-secrète-forte
  JWT_EXPIRATION: 86400000
```

### Modifier le port
```yaml
ports:
  - "8080:8080"  # Format: machine-host:conteneur
  # Pour changer le port hôte: "9090:8080"
```

## 📊 Monitoring

### Vérifier les ressources utilisées
```powershell
# Stats en temps réel
docker stats

# Avec le Ctrl+C pour quitter
```

### Health checks
```powershell
# Vérifier l'état de santé de l'API
docker exec medical-api curl http://localhost:8080/actuator/health

# Résultat attendu:
# {"status":"UP"}
```

## 🚨 Troubleshooting

### Erreur: Port 8080 déjà utilisé
```powershell
# Modifier le port dans docker-compose.yml
ports:
  - "9000:8080"  # Utiliser le port 9000 au lieu de 8080

# Redémarrer
docker-compose restart
```

### Erreur: MongoDB connection refused
```powershell
# Vérifier que MongoDB est démarré
docker-compose ps mongodb

# Si arrêté, relancer
docker-compose start mongodb

# Attendre que le health check soit OK
Start-Sleep -Seconds 15
```

### Effacer tout et recommencer
```powershell
# Arrêter et nettoyer
docker-compose down -v

# Relancer
docker-compose up -d
```

### Voir les erreurs de démarrage
```powershell
# Afficher les logs du dernier démarrage
docker-compose logs

# Relancer avec les logs visibles
docker-compose up
# Ctrl+C pour quitter
```

## 🔐 Sécurité en production

1. **Changer les identifiants MongoDB**:
   ```yaml
   MONGO_INITDB_ROOT_USERNAME: adminuser
   MONGO_INITDB_ROOT_PASSWORD: strongpassword123!
   ME_CONFIG_MONGODB_ADMINPASSWORD: strongpassword123!
   ```

2. **Changer la clé JWT**:
   ```yaml
   JWT_SECRET: une-clé-très-forte-de-32+ caractères-sans-espaces
   ```

3. **Désactiver Mongo Express en production**:
   ```yaml
   # Commenter/supprimer le service mongo-express
   # mongo-express:
   #   image: mongo-express:latest
   ```

4. **Utiliser des secrets Docker/Kubernetes**:
   ```powershell
   # Créer un fichier .env
   echo "MONGO_PASSWORD=strongpassword" > .env
   
   # Dans docker-compose.yml:
   # MONGO_INITDB_ROOT_PASSWORD: ${MONGO_PASSWORD}
   ```

## 📈 Scaling

### Créer plusieurs instances de l'API
```yaml
# Dans docker-compose.yml
medical-api-1:
  # ... configuration ...
  container_name: medical-api-1
  ports:
    - "8080:8080"

medical-api-2:
  # ... configuration ...
  container_name: medical-api-2
  ports:
    - "8081:8080"
```

### Avec un reverse proxy (Nginx)
```yaml
nginx:
  image: nginx:latest
  ports:
    - "80:80"
  volumes:
    - ./nginx.conf:/etc/nginx/nginx.conf:ro
```

## 🧹 Maintenance

### Nettoyer les images inutilisées
```powershell
# Faire un nettoyage complet
docker system prune -a

# Avec les volumes
docker system prune -a --volumes
```

### Sauvegarder les données MongoDB
```powershell
# Créer une backup
docker exec medical-mongodb mongodump --uri "mongodb://mongouser:mongopass@localhost:27017" --out /data/backup

# Restaurer
docker exec medical-mongodb mongorestore /data/backup
```

### Mettre à jour l'image
```powershell
# Reconstruire l'image Docker
docker-compose build --no-cache

# Redémarrer avec la nouvelle image
docker-compose up -d --force-recreate
```

## 📚 Documentation supplémentaire

- **Docker Documentation**: https://docs.docker.com/
- **Docker Compose**: https://docs.docker.com/compose/
- **MongoDB Docker**: https://hub.docker.com/_/mongo
- **Mongo Express**: https://hub.docker.com/_/mongo-express

---

**Note**: Les données MongoDB sont persistées dans des volumes Docker. 
Elles survivalent aux redémarrages sauf si vous faites `docker-compose down -v`.

**Dernier déploiement**: Juillet 2024

