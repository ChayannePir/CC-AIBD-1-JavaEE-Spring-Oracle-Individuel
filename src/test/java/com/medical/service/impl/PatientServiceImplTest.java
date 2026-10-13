package com.medical.service.impl;

import com.medical.dto.PatientDTO;
import com.medical.entity.Patient;
import com.medical.exception.ResourceAlreadyExistsException;
import com.medical.exception.ResourceNotFoundException;
import com.medical.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PatientServiceImpl.
 * Utilise Mockito pour mocker les dépendances (repository).
 * Teste les différents cas d'usage du service.
 *
 * Conventions:
 * - @DisplayName: Nom lisible du test
 * - given/when/then: Structure des tests
 * - Tester les cas heureux ET les cas d'erreur
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PatientService Tests")
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientDTO patientDTO;
    private Patient patient;
    private Pageable pageable;

    /**
     * Initialisation avant chaque test
     */
    @BeforeEach
    void setUp() {
        // Créer des données de test
        patientDTO = PatientDTO.builder()
                .id("507f1f77bcf86cd799439011")
                .numeroSS("1234567890123")
                .nomPAT("Jean Dupont")
                .build();

        patient = new Patient(
                "507f1f77bcf86cd799439011",
                "1234567890123",
                "Jean Dupont"
        );

        pageable = PageRequest.of(0, 20);
    }

    /**
     * Test: Créer un patient avec succès
     */
    @Test
    @DisplayName("Créer un patient avec succès")
    void testCreatePatient_Success() {
        // Given: Le patient n'existe pas déjà et le repository retournera l'entité sauvegardée
        when(patientRepository.findByNumeroSS(patientDTO.getNumeroSS()))
                .thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);

        // When: Créer le patient
        PatientDTO result = patientService.createPatient(patientDTO);

        // Then: Vérifier que le patient a été créé
        assertNotNull(result);
        assertEquals(patientDTO.getNumeroSS(), result.getNumeroSS());
        assertEquals(patientDTO.getNomPAT(), result.getNomPAT());
        verify(patientRepository).save(any(Patient.class));
    }

    /**
     * Test: Créer un patient avec un numéro SS qui existe déjà
     */
    @Test
    @DisplayName("Créer un patient avec numero SS existant - Exception")
    void testCreatePatient_AlreadyExists() {
        // Given: Le patient existe déjà
        when(patientRepository.findByNumeroSS(patientDTO.getNumeroSS()))
                .thenReturn(Optional.of(patient));

        // When/Then: Lever une exception
        assertThrows(ResourceAlreadyExistsException.class,
                () -> patientService.createPatient(patientDTO));
    }

    /**
     * Test: Récupérer un patient par ID avec succès
     */
    @Test
    @DisplayName("Récupérer un patient par ID avec succès")
    void testGetPatientById_Success() {
        // Given: Le patient existe
        when(patientRepository.findById(patient.getId()))
                .thenReturn(Optional.of(patient));

        // When: Récupérer le patient
        PatientDTO result = patientService.getPatientById(patient.getId());

        // Then: Vérifier que les données sont correctes
        assertNotNull(result);
        assertEquals(patient.getNumeroSS(), result.getNumeroSS());
        assertEquals(patient.getNomPAT(), result.getNomPAT());
    }

    /**
     * Test: Récupérer un patient inexistant
     */
    @Test
    @DisplayName("Récupérer un patient inexistant - Exception")
    void testGetPatientById_NotFound() {
        // Given: Le patient n'existe pas
        when(patientRepository.findById("invalid-id"))
                .thenReturn(Optional.empty());

        // When/Then: Lever une exception ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientById("invalid-id"));
    }

    /**
     * Test: Récupérer tous les patients avec pagination
     */
    @Test
    @DisplayName("Récupérer tous les patients avec pagination")
    void testGetAllPatients() {
        // Given: Une page avec des patients
        Page<Patient> page = new PageImpl<>(
                new ArrayList<>() {{
                    add(patient);
                }},
                pageable,
                1
        );
        when(patientRepository.findAll(pageable))
                .thenReturn(page);

        // When: Récupérer tous les patients
        Page<PatientDTO> result = patientService.getAllPatients(pageable);

        // Then: Vérifier les résultats
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(patientRepository).findAll(pageable);
    }

    /**
     * Test: Supprimer un patient avec succès
     */
    @Test
    @DisplayName("Supprimer un patient avec succès")
    void testDeletePatient_Success() {
        // Given: Le patient existe
        when(patientRepository.existsById(patient.getId()))
                .thenReturn(true);

        // When: Supprimer le patient
        patientService.deletePatient(patient.getId());

        // Then: Vérifier que la suppression a été appelée
        verify(patientRepository).deleteById(patient.getId());
    }

    /**
     * Test: Supprimer un patient inexistant
     */
    @Test
    @DisplayName("Supprimer un patient inexistant - Exception")
    void testDeletePatient_NotFound() {
        // Given: Le patient n'existe pas
        when(patientRepository.existsById("invalid-id"))
                .thenReturn(false);

        // When/Then: Lever une exception
        assertThrows(ResourceNotFoundException.class,
                () -> patientService.deletePatient("invalid-id"));
    }
}

