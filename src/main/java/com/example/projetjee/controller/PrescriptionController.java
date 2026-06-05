package com.example.projetjee.controller;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Medicament;
import com.example.projetjee.model.Prescription;
import com.example.projetjee.service.ConsultationService;
import com.example.projetjee.service.MedicamentService;
import com.example.projetjee.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final ConsultationService consultationService;
    private final MedicamentService medicamentService;

    public PrescriptionController(PrescriptionService prescriptionService, ConsultationService consultationService, MedicamentService medicamentService) {
        this.prescriptionService = prescriptionService;
        this.consultationService = consultationService;
        this.medicamentService = medicamentService;
    }

    // Add a new prescription to a consultation
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        // Validate if consultation and medicament exist
        Optional<Consultation> consultationOptional = consultationService.getConsultationById(prescription.getConsultation().getNumero());
        Optional<Medicament> medicamentOptional = medicamentService.getMedicamentById(prescription.getMedicament().getCode());

        if (consultationOptional.isEmpty() || medicamentOptional.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Or a more specific error message
        }

        prescription.setConsultation(consultationOptional.get());
        prescription.setMedicament(medicamentOptional.get());

        Prescription savedPrescription = prescriptionService.savePrescription(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrescription);
    }

    // Update an existing prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        Optional<Prescription> existingPrescriptionOptional = prescriptionService.getPrescriptionById(id);
        if (existingPrescriptionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Validate if consultation and medicament exist
        Optional<Consultation> consultationOptional = consultationService.getConsultationById(prescription.getConsultation().getNumero());
        Optional<Medicament> medicamentOptional = medicamentService.getMedicamentById(prescription.getMedicament().getCode());

        if (consultationOptional.isEmpty() || medicamentOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        prescription.setId(id); // Ensure the ID is set for update
        prescription.setConsultation(consultationOptional.get());
        prescription.setMedicament(medicamentOptional.get());

        Prescription updatedPrescription = prescriptionService.savePrescription(prescription);
        return ResponseEntity.ok(updatedPrescription);
    }

    // Delete a prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (!prescriptionService.getPrescriptionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
