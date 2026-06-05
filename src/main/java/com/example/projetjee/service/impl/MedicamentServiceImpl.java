package com.example.projetjee.service.impl;

import com.example.projetjee.model.Medicament;
import com.example.projetjee.repository.MedicamentRepository;
import com.example.projetjee.service.MedicamentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;

    public MedicamentServiceImpl(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public Page<Medicament> getAllMedicaments(Pageable pageable) {
        return medicamentRepository.findAll(pageable);
    }

    @Override
    public Optional<Medicament> getMedicamentById(Long code) {
        return medicamentRepository.findById(code);
    }

    @Override
    public Medicament saveMedicament(Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    @Override
    public void deleteMedicament(Long code) {
        medicamentRepository.deleteById(code);
    }
}
