package com.example.projetjee.service;

import com.example.projetjee.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
    Page<Patient> getAllPatients(Pageable pageable);
    Page<Patient> searchPatients(String searchTerm, Pageable pageable);
    Optional<Patient> getPatientById(Long nss);
    Patient savePatient(Patient patient);
    void deletePatient(Long nss);
}
