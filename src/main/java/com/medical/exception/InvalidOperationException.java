package com.medical.exception;

/**
 * Exception levée quand une opération n'est pas autorisée.
 * Utilisée pour les validations métier spécifiques.
 *
 * Hérite de RuntimeException pour être une exception non vérifiée.
 */
public class InvalidOperationException extends RuntimeException {

    /**
     * Constructeur avec message
     *
     * @param message le message d'erreur
     */
    public InvalidOperationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

