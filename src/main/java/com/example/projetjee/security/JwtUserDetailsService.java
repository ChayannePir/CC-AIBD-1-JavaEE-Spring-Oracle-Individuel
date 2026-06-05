package com.example.projetjee.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Définition d'un utilisateur de test conforme à votre application.properties
        if ("user".equals(username)) {
            return new User(
                    "user",
                    // Mot de passe "password" encodé avec l'algorithme BCrypt
                    "$2a$10$vCXMWX7Sj87656qD9fKGCu1A.K0vD2IAnGpwKpA.B75p6gNbeDWea",
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom: " + username);
        }
    }
}