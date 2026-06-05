package com.example.projetjee.service;

import com.example.projetjee.model.Medicament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedicamentService {
    Page<Medicament> getAllMedicaments(Pageable pageable);
    Optional<Medicament> getMedicamentById(Long code);
    Medicament saveMedicament(Medicament medicament);
    void deleteMedicament(Long code);
}
