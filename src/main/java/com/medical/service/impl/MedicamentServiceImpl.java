package com.medical.service.impl;

import com.medical.dto.MedicamentDTO;
import com.medical.entity.Medicament;
import com.medical.exception.ResourceAlreadyExistsException;
import com.medical.exception.ResourceNotFoundException;
import com.medical.repository.MedicamentRepository;
import com.medical.service.MedicamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation de l'interface MedicamentService.
 * Fournit la logique métier pour la gestion des médicaments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> getAllMedicaments(Pageable pageable) {
        log.info("Récupération de tous les médicaments - Page: {}, Size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());

        return medicamentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> searchMedicaments(String libelle, Pageable pageable) {
        log.info("Recherche de médicaments - Libelle: {}", libelle);

        if (libelle != null && !libelle.isEmpty()) {
            return medicamentRepository.findByLibelleContainingIgnoreCase(libelle, pageable)
                    .map(this::convertToDTO);
        }

        return medicamentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicamentDTO getMedicamentById(String id) {
        log.info("Récupération du médicament avec l'ID: {}", id);

        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médicament avec l'ID " + id + " non trouvé"));

        return convertToDTO(medicament);
    }

    @Override
    @Transactional
    public MedicamentDTO createMedicament(MedicamentDTO medicamentDTO) {
        log.info("Création d'un nouveau médicament avec le code: {}", medicamentDTO.getCode());

        if (medicamentRepository.findByCode(medicamentDTO.getCode()).isPresent()) {
            log.warn("Un médicament avec le code {} existe déjà", medicamentDTO.getCode());
            throw new ResourceAlreadyExistsException(
                    "Un médicament avec le code " + medicamentDTO.getCode() + " existe déjà");
        }

        Medicament medicament = convertToEntity(medicamentDTO);
        Medicament savedMedicament = medicamentRepository.save(medicament);

        log.info("Médicament créé avec succès - ID: {}", savedMedicament.getId());
        return convertToDTO(savedMedicament);
    }

    @Override
    @Transactional
    public MedicamentDTO updateMedicament(String id, MedicamentDTO medicamentDTO) {
        log.info("Mise à jour du médicament avec l'ID: {}", id);

        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médicament avec l'ID " + id + " non trouvé"));

        medicament.setCode(medicamentDTO.getCode());
        medicament.setLibelle(medicamentDTO.getLibelle());

        Medicament updatedMedicament = medicamentRepository.save(medicament);

        log.info("Médicament mis à jour avec succès - ID: {}", id);
        return convertToDTO(updatedMedicament);
    }

    @Override
    @Transactional
    public void deleteMedicament(String id) {
        log.info("Suppression du médicament avec l'ID: {}", id);

        if (!medicamentRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un médicament inexistant - ID: {}", id);
            throw new ResourceNotFoundException(
                    "Médicament avec l'ID " + id + " non trouvé");
        }

        medicamentRepository.deleteById(id);
        log.info("Médicament supprimé avec succès - ID: {}", id);
    }

    private MedicamentDTO convertToDTO(Medicament medicament) {
        return MedicamentDTO.builder()
                .id(medicament.getId())
                .code(medicament.getCode())
                .libelle(medicament.getLibelle())
                .build();
    }

    private Medicament convertToEntity(MedicamentDTO medicamentDTO) {
        return new Medicament(
                null,
                medicamentDTO.getCode(),
                medicamentDTO.getLibelle()
        );
    }
}

