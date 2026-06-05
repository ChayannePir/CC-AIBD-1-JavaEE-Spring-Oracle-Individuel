package com.example.projetjee.service;

import com.example.projetjee.model.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedecinService {
    Page<Medecin> getAllMedecins(Pageable pageable);
    Page<Medecin> searchMedecins(String searchTerm, Pageable pageable);
    Optional<Medecin> getMedecinById(Long matricule);
    Medecin saveMedecin(Medecin medecin);
    void deleteMedecin(Long matricule);
}
