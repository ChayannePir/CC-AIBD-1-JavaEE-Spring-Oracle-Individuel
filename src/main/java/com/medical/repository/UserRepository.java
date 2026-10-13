package com.medical.repository;

import com.medical.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité User.
 * Fournit les méthodes de gestion des utilisateurs dans la base de données MongoDB.
 *
 * Hérite de MongoRepository qui fournit:
 * - Opérations CRUD de base (save, delete, findAll, findById)
 *
 * Méthodes personnalisées:
 * - findByUsername: Recherche un utilisateur par son nom d'utilisateur
 * - findByEmail: Recherche un utilisateur par son email
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Recherche un utilisateur par son nom d'utilisateur
     * Utilisé principalement pour l'authentification
     *
     * @param username le nom d'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Recherche un utilisateur par son email
     *
     * @param email l'email de l'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);
}

