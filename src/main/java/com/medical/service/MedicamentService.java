package com.medical.service;

import com.medical.dto.MedicamentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de service pour la gestion des médicaments.
 * Gère les opérations de gestion des médicaments utilisés dans les consultations.
 */
public interface MedicamentService {

    /**
     * Récupère tous les médicaments avec pagination.
     */
    Page<MedicamentDTO> getAllMedicaments(Pageable pageable);

    /**
     * Recherche les médicaments par libellé.
     */
    Page<MedicamentDTO> searchMedicaments(String libelle, Pageable pageable);

    /**
     * Récupère un médicament par son ID.
     */
    MedicamentDTO getMedicamentById(String id);

    /**
     * Crée un nouveau médicament.
     */
    MedicamentDTO createMedicament(MedicamentDTO medicamentDTO);

    /**
     * Met à jour un médicament.
     */
    MedicamentDTO updateMedicament(String id, MedicamentDTO medicamentDTO);

    /**
     * Supprime un médicament.
     */
    void deleteMedicament(String id);
}

