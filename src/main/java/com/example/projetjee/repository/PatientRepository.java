package com.example.projetjee.repository;

import com.example.projetjee.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByNomPATContainingIgnoreCase(String nomPAT, Pageable pageable);
    Page<Patient> findByNSS(Long nss, Pageable pageable);
}
