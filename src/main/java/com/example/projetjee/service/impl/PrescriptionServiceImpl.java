package com.example.projetjee.service.impl;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Prescription;
import com.example.projetjee.repository.PrescriptionRepository;
import com.example.projetjee.service.PrescriptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public List<Prescription> getPrescriptionsByConsultation(Consultation consultation) {
        return prescriptionRepository.findByConsultation(consultation);
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}
