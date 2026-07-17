package com.medical.exception;

/**
 * Exception levée quand une ressource demandée n'est pas trouvée.
 * Utilisée pour les cas 404 Not Found.
 *
 * Hérite de RuntimeException pour être une exception non vérifiée.
 * Permet une meilleure gestion avec les contrôleurs advice.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur avec message
     *
     * @param message le message d'erreur
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

