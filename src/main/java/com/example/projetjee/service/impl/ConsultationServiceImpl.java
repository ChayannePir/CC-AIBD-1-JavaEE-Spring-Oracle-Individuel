package com.example.projetjee.service.impl;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Patient;
import com.example.projetjee.repository.ConsultationRepository;
import com.example.projetjee.service.ConsultationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public Page<Consultation> getConsultationsByPatient(Patient patient, Pageable pageable) {
        return consultationRepository.findByPatient(patient, pageable);
    }

    @Override
    public Optional<Consultation> getConsultationById(Long numero) {
        return consultationRepository.findById(numero);
    }

    @Override
    public Consultation saveConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public void deleteConsultation(Long numero) {
        consultationRepository.deleteById(numero);
    }

    @Override
    public boolean isConsultationProgrammable(Consultation consultation) {
        // A consultation is programmable if its date is in the future
        return consultation.getDate() != null && consultation.getDate().isAfter(LocalDate.now());
    }
}
