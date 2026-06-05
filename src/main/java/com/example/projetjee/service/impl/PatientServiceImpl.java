package com.example.projetjee.service.impl;

import com.example.projetjee.model.Patient;
import com.example.projetjee.repository.PatientRepository;
import com.example.projetjee.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Page<Patient> searchPatients(String searchTerm, Pageable pageable) {
        // Assuming searchTerm can be either nomPAT or NSS
        try {
            Long nss = Long.parseLong(searchTerm);
            return patientRepository.findByNSS(nss, pageable);
        } catch (NumberFormatException e) {
            return patientRepository.findByNomPATContainingIgnoreCase(searchTerm, pageable);
        }
    }

    @Override
    public Optional<Patient> getPatientById(Long nss) {
        return patientRepository.findById(nss);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long nss) {
        patientRepository.deleteById(nss);
    }
}
