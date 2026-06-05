package com.example.projetjee.repository;

import com.example.projetjee.model.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    Page<Medecin> findByNomMEDContainingIgnoreCase(String nomMED, Pageable pageable);
}
