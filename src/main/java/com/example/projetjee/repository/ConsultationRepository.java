package com.example.projetjee.repository;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Page<Consultation> findByPatient(Patient patient, Pageable pageable);
}
