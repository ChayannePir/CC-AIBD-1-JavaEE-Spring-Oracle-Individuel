package com.example.projetjee.controller;

import com.example.projetjee.model.Medecin;
import com.example.projetjee.service.MedecinService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/medecins")
public class MedecinController {

    private final MedecinService medecinService;

    public MedecinController(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    @GetMapping
    public ResponseEntity<Page<Medecin>> getAllMedecins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Medecin> medecins;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            medecins = medecinService.searchMedecins(searchTerm, pageable);
        } else {
            medecins = medecinService.getAllMedecins(pageable);
        }
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<Medecin> getMedecinById(@PathVariable Long matricule) {
        Optional<Medecin> medecin = medecinService.getMedecinById(matricule);
        return medecin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medecin> createMedecin(@RequestBody Medecin medecin) {
        Medecin savedMedecin = medecinService.saveMedecin(medecin);
        return ResponseEntity.ok(savedMedecin);
    }

    @PutMapping("/{matricule}")
    public ResponseEntity<Medecin> updateMedecin(@PathVariable Long matricule, @RequestBody Medecin medecin) {
        if (!medecinService.getMedecinById(matricule).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medecin.setMatricule(matricule); // Ensure the ID is set for update
        Medecin updatedMedecin = medecinService.saveMedecin(medecin);
        return ResponseEntity.ok(updatedMedecin);
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long matricule) {
        if (!medecinService.getMedecinById(matricule).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medecinService.deleteMedecin(matricule);
        return ResponseEntity.noContent().build();
    }
}
