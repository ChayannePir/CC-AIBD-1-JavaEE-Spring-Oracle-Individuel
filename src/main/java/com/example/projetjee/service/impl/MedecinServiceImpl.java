package com.example.projetjee.service.impl;

import com.example.projetjee.model.Medecin;
import com.example.projetjee.repository.MedecinRepository;
import com.example.projetjee.service.MedecinService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;

    public MedecinServiceImpl(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    @Override
    public Page<Medecin> getAllMedecins(Pageable pageable) {
        return medecinRepository.findAll(pageable);
    }

    @Override
    public Page<Medecin> searchMedecins(String searchTerm, Pageable pageable) {
        try {
            Long matricule = Long.parseLong(searchTerm);
            return medecinRepository.findById(matricule).map(medecin -> (Page<Medecin>) new org.springframework.data.domain.PageImpl<>(java.util.Collections.singletonList(medecin), pageable, 1)).orElseGet(() -> Page.empty(pageable));
        } catch (NumberFormatException e) {
            return medecinRepository.findByNomMEDContainingIgnoreCase(searchTerm, pageable);
        }
    }

    @Override
    public Optional<Medecin> getMedecinById(Long matricule) {
        return medecinRepository.findById(matricule);
    }

    @Override
    public Medecin saveMedecin(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    @Override
    public void deleteMedecin(Long matricule) {
        medecinRepository.deleteById(matricule);
    }
}
