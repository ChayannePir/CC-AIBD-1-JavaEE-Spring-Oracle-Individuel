# Dockerfile pour Medical API
# Construit l'image Docker de l'application

# Étape 1: Builder avec Maven
FROM maven:3.9.2-eclipse-temurin-21 AS builder

WORKDIR /build

# Copier le pom.xml
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2: Runtime avec l'application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le JAR depuis l'étape de builder
COPY --from=builder /build/target/medical-api-1.0.0.jar app.jar

# Créer les répertoires nécessaires
RUN mkdir -p logs documents

# Port d'écoute
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

