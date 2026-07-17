package com.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Entité User représentant un utilisateur du système pour l'authentification.
 * Mappée à la collection MongoDB 'users'.
 * Implémente UserDetails de Spring Security pour l'intégration avec le framework.
 *
 * Attributs:
 * - id: Identifiant unique de l'utilisateur
 * - username: Nom d'utilisateur unique
 * - password: Mot de passe chiffré
 * - email: Email de l'utilisateur
 * - roles: Liste des rôles de l'utilisateur (ADMIN, DOCTOR, PATIENT, etc.)
 * - actif: Indique si le compte est actif
 *
 * Implémentation de UserDetails pour Spring Security:
 * - getUsername(), getPassword()
 * - getAuthorities() - retourne les autorités basées sur les rôles
 * - isEnabled(), isAccountNonExpired(), etc.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique de l'utilisateur (généré par MongoDB)
     */
    @Id
    private String id;

    /**
     * Nom d'utilisateur unique
     * Non null
     */
    private String username;

    /**
     * Mot de passe chiffré (utiliser BCryptPasswordEncoder)
     * Non null
     */
    private String password;

    /**
     * Email de l'utilisateur
     * Non null
     */
    private String email;

    /**
     * Liste des rôles de l'utilisateur
     * Exemples: ROLE_ADMIN, ROLE_DOCTOR, ROLE_PATIENT
     * Par défaut contient ROLE_USER
     */
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    /**
     * Indique si le compte est actif
     * Par défaut: true
     */
    @Builder.Default
    private Boolean actif = true;

    /**
     * Retourne les autorités (rôles) de l'utilisateur
     * Convertit la liste des rôles en GrantedAuthority pour Spring Security
     *
     * @return Collection des autorités
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    /**
     * Indique si le compte est actif
     *
     * @return true si le compte est actif, false sinon
     */
    @Override
    public boolean isEnabled() {
        return actif;
    }

    /**
     * Indique si le compte n'est pas expiré
     *
     * @return true (jamais expiré dans cette implémentation)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si les credentials ne sont pas expirés
     *
     * @return true (jamais expiré dans cette implémentation)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si le compte n'est pas verrouillé
     *
     * @return true (jamais verrouillé dans cette implémentation)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
}

