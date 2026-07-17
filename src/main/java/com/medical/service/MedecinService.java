package com.medical.service;

import com.medical.dto.MedecinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de service pour la gestion des médecins.
 * Définit les opérations métier disponibles sur les médecins.
 */
public interface MedecinService {

    /**
     * Récupère tous les médecins avec pagination.
     */
    Page<MedecinDTO> getAllMedecins(Pageable pageable);

    /**
     * Recherche les médecins par matricule et/ou nom.
     */
    Page<MedecinDTO> searchMedecins(String matricule, String nomMED, Pageable pageable);

    /**
     * Récupère un médecin par son ID.
     */
    MedecinDTO getMedecinById(String id);

    /**
     * Crée un nouveau médecin.
     */
    MedecinDTO createMedecin(MedecinDTO medecinDTO);

    /**
     * Met à jour un médecin.
     */
    MedecinDTO updateMedecin(String id, MedecinDTO medecinDTO);

    /**
     * Supprime un médecin.
     */
    void deleteMedecin(String id);
}

