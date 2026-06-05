package com.example.projetjee.repository;

import com.example.projetjee.model.Prescription;
import com.example.projetjee.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByConsultation(Consultation consultation);
}
