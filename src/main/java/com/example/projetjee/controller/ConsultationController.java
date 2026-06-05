package com.example.projetjee.controller;

import com.example.projetjee.model.Consultation;
import com.example.projetjee.model.Patient;
import com.example.projetjee.model.Prescription;
import com.example.projetjee.service.ConsultationService;
import com.example.projetjee.service.PatientService;
import com.example.projetjee.service.PrescriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    public ConsultationController(ConsultationService consultationService, PatientService patientService, PrescriptionService prescriptionService) {
        this.consultationService = consultationService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    // List consultations for a given patient with pagination
    @GetMapping("/patient/{patientNss}")
    public ResponseEntity<Page<Consultation>> getConsultationsByPatient(
            @PathVariable Long patientNss,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Optional<Patient> patientOptional = patientService.getPatientById(patientNss);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Consultation> consultations = consultationService.getConsultationsByPatient(patientOptional.get(), pageable);
        return ResponseEntity.ok(consultations);
    }

    // Detail prescribed medications in a consultation
    @GetMapping("/{consultationNumero}/prescriptions")
    public ResponseEntity<List<Prescription>> getPrescriptionsForConsultation(@PathVariable Long consultationNumero) {
        Optional<Consultation> consultationOptional = consultationService.getConsultationById(consultationNumero);
        if (consultationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByConsultation(consultationOptional.get());
        return ResponseEntity.ok(prescriptions);
    }

    // Add a programmed consultation
    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation) {
        if (!consultationService.isConsultationProgrammable(consultation)) {
            return ResponseEntity.badRequest().body(null); // Only future consultations can be programmed
        }
        Consultation savedConsultation = consultationService.saveConsultation(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConsultation);
    }

    // Modify a programmed consultation
    @PutMapping("/{numero}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long numero, @RequestBody Consultation consultation) {
        Optional<Consultation> existingConsultationOptional = consultationService.getConsultationById(numero);
        if (existingConsultationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Consultation existingConsultation = existingConsultationOptional.get();
        if (!consultationService.isConsultationProgrammable(existingConsultation)) {
            return ResponseEntity.badRequest().body(null); // Cannot modify past consultations
        }

        consultation.setNumero(numero); // Ensure the ID is set for update
        Consultation updatedConsultation = consultationService.saveConsultation(consultation);
        return ResponseEntity.ok(updatedConsultation);
    }

    // Delete a programmed consultation
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long numero) {
        Optional<Consultation> existingConsultationOptional = consultationService.getConsultationById(numero);
        if (existingConsultationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Consultation existingConsultation = existingConsultationOptional.get();
        if (!consultationService.isConsultationProgrammable(existingConsultation)) {
            return ResponseEntity.badRequest().build(); // Cannot delete past consultations
        }
        consultationService.deleteConsultation(numero);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for attaching a document (placeholder)
    @PostMapping("/{consultationNumero}/document")
    public ResponseEntity<String> uploadDocument(@PathVariable Long consultationNumero, @RequestParam("file") MultipartFile file) {
        Optional<Consultation> consultationOptional = consultationService.getConsultationById(consultationNumero);
        if (consultationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // In a real application, you would save the file to a storage system (e.g., local disk, S3, Azure Blob Storage)
        // and store the file path/metadata in the database, possibly in the Consultation entity or a new Document entity.
        // For now, this is just a placeholder.
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            // Logic to save file and update consultation with document reference
            return ResponseEntity.ok("Document " + fileName + " uploaded successfully for consultation " + consultationNumero);
        } else {
            return ResponseEntity.badRequest().body("No file provided.");
        }
    }
}
