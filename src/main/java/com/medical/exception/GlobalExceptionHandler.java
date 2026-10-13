package com.medical.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions de l'API.
 * Annotée avec @RestControllerAdvice pour intercepter toutes les exceptions de tous les contrôleurs.
 *
 * Gère:
 * - ResourceNotFoundException: Resource non trouvée (404)
 * - ResourceAlreadyExistsException: Resource déjà existe (409 Conflict)
 * - InvalidOperationException: Opération invalide (400 Bad Request)
 * - BadCredentialsException: Identifiants incorrects (401 Unauthorized)
 * - MethodArgumentNotValidException: Validation des paramètres échouée (400)
 * - Exception générique: Erreur serveur (500)
 *
 * Chaque handler retourne une ErrorResponse standardisée avec:
 * - Code HTTP approprié
 * - Message d'erreur clair
 * - Timestamp
 * - Chemin de la requête
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère l'exception ResourceNotFoundException (404 Not Found)
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        // Enregistrer l'erreur dans les logs
        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère l'exception ResourceAlreadyExistsException (409 Conflict)
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 409
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex, WebRequest request) {

        log.warn("Resource already exists: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Gère l'exception InvalidOperationException (400 Bad Request)
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 400
     */
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(
            InvalidOperationException ex, WebRequest request) {

        log.warn("Invalid operation: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs d'authentification (401 Unauthorized)
     * BadCredentialsException levée quand username/password sont incorrects
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {

        log.warn("Bad credentials provided");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Identifiants incorrects")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gère les erreurs de validation des paramètres (400 Bad Request)
     * Levée quand @Valid échoue sur les paramètres de requête
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Collecter tous les messages d'erreur de validation
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", message);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Erreur de validation: " + message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non capturées (500 Internal Server Error)
     * Catch-all pour les erreurs inattendues
     *
     * @param ex l'exception levée
     * @param request la requête web
     * @return ResponseEntity avec ErrorResponse et code 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        // Enregistrer l'erreur complète dans les logs (importante pour le debug)
        log.error("Une erreur interne s'est produite", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Une erreur interne s'est produite. Veuillez réessayer plus tard.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

