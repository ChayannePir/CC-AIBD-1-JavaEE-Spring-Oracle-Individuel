package com.medical.service.impl;

import com.medical.dto.MedecinDTO;
import com.medical.entity.Medecin;
import com.medical.exception.ResourceAlreadyExistsException;
import com.medical.exception.ResourceNotFoundException;
import com.medical.repository.MedecinRepository;
import com.medical.service.MedecinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation de l'interface MedecinService.
 * Fournit la logique métier pour la gestion des médecins.
 * Utilise le pattern de conversion DTO <-> Entité.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<MedecinDTO> getAllMedecins(Pageable pageable) {
        log.info("Récupération de tous les médecins - Page: {}, Size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());

        return medecinRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedecinDTO> searchMedecins(String matricule, String nomMED, Pageable pageable) {
        log.info("Recherche de médecins - Matricule: {}, NomMED: {}", matricule, nomMED);

        if (matricule != null && !matricule.isEmpty() && nomMED != null && !nomMED.isEmpty()) {
            return medecinRepository.findByMatriculeContainingIgnoreCaseOrNomMEDContainingIgnoreCase(
                    matricule, nomMED, pageable)
                    .map(this::convertToDTO);
        }

        if (matricule != null && !matricule.isEmpty()) {
            return medecinRepository.findByMatriculeContainingIgnoreCaseOrNomMEDContainingIgnoreCase(
                    matricule, "", pageable)
                    .map(this::convertToDTO);
        }

        if (nomMED != null && !nomMED.isEmpty()) {
            return medecinRepository.findByNomMEDContainingIgnoreCase(nomMED, pageable)
                    .map(this::convertToDTO);
        }

        return medecinRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MedecinDTO getMedecinById(String id) {
        log.info("Récupération du médecin avec l'ID: {}", id);

        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médecin avec l'ID " + id + " non trouvé"));

        return convertToDTO(medecin);
    }

    @Override
    @Transactional
    public MedecinDTO createMedecin(MedecinDTO medecinDTO) {
        log.info("Création d'un nouveau médecin avec le matricule: {}", medecinDTO.getMatricule());

        if (medecinRepository.findByMatricule(medecinDTO.getMatricule()).isPresent()) {
            log.warn("Un médecin avec le matricule {} existe déjà", medecinDTO.getMatricule());
            throw new ResourceAlreadyExistsException(
                    "Un médecin avec le matricule " + medecinDTO.getMatricule() + " existe déjà");
        }

        Medecin medecin = convertToEntity(medecinDTO);
        Medecin savedMedecin = medecinRepository.save(medecin);

        log.info("Médecin créé avec succès - ID: {}", savedMedecin.getId());
        return convertToDTO(savedMedecin);
    }

    @Override
    @Transactional
    public MedecinDTO updateMedecin(String id, MedecinDTO medecinDTO) {
        log.info("Mise à jour du médecin avec l'ID: {}", id);

        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médecin avec l'ID " + id + " non trouvé"));

        medecin.setMatricule(medecinDTO.getMatricule());
        medecin.setNomMED(medecinDTO.getNomMED());

        Medecin updatedMedecin = medecinRepository.save(medecin);

        log.info("Médecin mis à jour avec succès - ID: {}", id);
        return convertToDTO(updatedMedecin);
    }

    @Override
    @Transactional
    public void deleteMedecin(String id) {
        log.info("Suppression du médecin avec l'ID: {}", id);

        if (!medecinRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un médecin inexistant - ID: {}", id);
            throw new ResourceNotFoundException(
                    "Médecin avec l'ID " + id + " non trouvé");
        }

        medecinRepository.deleteById(id);
        log.info("Médecin supprimé avec succès - ID: {}", id);
    }

    private MedecinDTO convertToDTO(Medecin medecin) {
        return MedecinDTO.builder()
                .id(medecin.getId())
                .matricule(medecin.getMatricule())
                .nomMED(medecin.getNomMED())
                .build();
    }

    private Medecin convertToEntity(MedecinDTO medecinDTO) {
        return new Medecin(
                null,
                medecinDTO.getMatricule(),
                medecinDTO.getNomMED()
        );
    }
}

