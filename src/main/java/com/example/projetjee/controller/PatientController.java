package com.example.projetjee.controller;

import com.example.projetjee.model.Patient;
import com.example.projetjee.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patients;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            patients = patientService.searchPatients(searchTerm, pageable);
        } else {
            patients = patientService.getAllPatients(pageable);
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{nss}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long nss) {
        Optional<Patient> patient = patientService.getPatientById(nss);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/{nss}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long nss, @RequestBody Patient patient) {
        if (!patientService.getPatientById(nss).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        patient.setNSS(nss); // Ensure the ID is set for update
        Patient updatedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{nss}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long nss) {
        if (!patientService.getPatientById(nss).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        patientService.deletePatient(nss);
        return ResponseEntity.noContent().build();
    }
}
