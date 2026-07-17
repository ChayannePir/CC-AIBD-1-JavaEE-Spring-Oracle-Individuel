package com.medical.initialization;

import com.medical.entity.Medecin;
import com.medical.entity.Patient;
import com.medical.entity.User;
import com.medical.repository.MedecinRepository;
import com.medical.repository.PatientRepository;
import com.medical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Classe d'initialisation des données de test.
 * S'exécute au démarrage de l'application pour peupler la base de données
 * avec des données de test si la collection est vide.
 *
 * Utilise CommandLineRunner pour exécuter du code au démarrage:
 * 1. Crée des patients de test
 * 2. Crée des médecins de test
 * 3. Crée des utilisateurs de test pour l'authentification
 *
 * Ces données permettent de tester l'API sans avoir à créer manuellement
 * les entités via les endpoints POST.
 */
@Configuration
@Slf4j
public class DataInitializer {

    /**
     * Initialise les données de test au démarrage de l'application.
     *
     * @param patientRepository le repository des patients
     * @param medecinRepository le repository des médecins
     * @param userRepository le repository des utilisateurs
     * @param passwordEncoder l'encodeur de mot de passe
     * @return un CommandLineRunner
     */
    @Bean
    public CommandLineRunner initData(PatientRepository patientRepository,
                                      MedecinRepository medecinRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {
            // Initialiser les patients
            if (patientRepository.count() == 0) {
                log.info("Création des patients de test...");

                Patient patient1 = new Patient(null, "1234567890123", "Jean Dupont");
                Patient patient2 = new Patient(null, "2345678901234", "Marie Martin");
                Patient patient3 = new Patient(null, "3456789012345", "Pierre Bernard");
                Patient patient4 = new Patient(null, "4567890123456", "Sophie Leclerc");

                patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4));
                log.info("4 patients de test créés avec succès");
            }

            // Initialiser les médecins
            if (medecinRepository.count() == 0) {
                log.info("Création des médecins de test...");

                Medecin medecin1 = new Medecin(null, "MED001", "Dr. Martin Durand");
                Medecin medecin2 = new Medecin(null, "MED002", "Dr. Hélène Rousseau");
                Medecin medecin3 = new Medecin(null, "MED003", "Dr. Laurent Petit");

                medecinRepository.saveAll(Arrays.asList(medecin1, medecin2, medecin3));
                log.info("3 médecins de test créés avec succès");
            }

            // Initialiser les utilisateurs
            if (userRepository.count() == 0) {
                log.info("Création des utilisateurs de test...");

                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@medical-api.fr")
                        .roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
                        .actif(true)
                        .build();

                User doctorUser = User.builder()
                        .username("doctor_001")
                        .password(passwordEncoder.encode("doctor123"))
                        .email("doctor@medical-api.fr")
                        .roles(Arrays.asList("ROLE_DOCTOR", "ROLE_USER"))
                        .actif(true)
                        .build();

                User patientUser = User.builder()
                        .username("patient_001")
                        .password(passwordEncoder.encode("patient123"))
                        .email("patient@medical-api.fr")
                        .roles(Arrays.asList("ROLE_PATIENT", "ROLE_USER"))
                        .actif(true)
                        .build();

                userRepository.saveAll(Arrays.asList(adminUser, doctorUser, patientUser));
                log.info("3 utilisateurs de test créés avec succès");

                log.info("=== Identifiants de test ===");
                log.info("Admin - username: admin | password: admin123");
                log.info("Doctor - username: doctor_001 | password: doctor123");
                log.info("Patient - username: patient_001 | password: patient123");
            }
        };
    }
}

