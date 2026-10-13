package com.medical.exception;

/**
 * Exception levée quand une ressource déjà existe.
 * Utilisée pour empêcher les doublons (ex: patient avec même numéro SS).
 *
 * Hérite de RuntimeException pour être une exception non vérifiée.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    /**
     * Constructeur avec message
     *
     * @param message le message d'erreur
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

