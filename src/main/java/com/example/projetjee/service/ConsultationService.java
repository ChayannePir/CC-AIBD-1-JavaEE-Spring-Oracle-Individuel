package com.example.projetjee.service;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ConsultationService {
    Page<Consultation> getConsultationsByPatient(Patient patient, Pageable pageable);
    Optional<Consultation> getConsultationById(Long numero);
    Consultation saveConsultation(Consultation consultation);
    void deleteConsultation(Long numero);
    boolean isConsultationProgrammable(Consultation consultation);
}
