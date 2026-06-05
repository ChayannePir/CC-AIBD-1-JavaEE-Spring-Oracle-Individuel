package com.example.projetjee.service;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    List<Prescription> getPrescriptionsByConsultation(Consultation consultation);
    Optional<Prescription> getPrescriptionById(Long id);
    Prescription savePrescription(Prescription prescription);
    void deletePrescription(Long id);
}
