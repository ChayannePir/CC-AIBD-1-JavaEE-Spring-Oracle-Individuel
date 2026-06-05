package com.example.projetjee.controller;

import com.example.projetjee.model.Medicament;
import com.example.projetjee.service.MedicamentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentController {

    private final MedicamentService medicamentService;

    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @GetMapping
    public ResponseEntity<Page<Medicament>> getAllMedicaments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Medicament> medicaments = medicamentService.getAllMedicaments(pageable);
        return ResponseEntity.ok(medicaments);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Medicament> getMedicamentById(@PathVariable Long code) {
        Optional<Medicament> medicament = medicamentService.getMedicamentById(code);
        return medicament.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicament> createMedicament(@RequestBody Medicament medicament) {
        Medicament savedMedicament = medicamentService.saveMedicament(medicament);
        return ResponseEntity.ok(savedMedicament);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Medicament> updateMedicament(@PathVariable Long code, @RequestBody Medicament medicament) {
        if (!medicamentService.getMedicamentById(code).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medicament.setCode(code); // Ensure the ID is set for update
        Medicament updatedMedicament = medicamentService.saveMedicament(medicament);
        return ResponseEntity.ok(updatedMedicament);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable Long code) {
        if (!medicamentService.getMedicamentById(code).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medicamentService.deleteMedicament(code);
        return ResponseEntity.noContent().build();
    }
}
