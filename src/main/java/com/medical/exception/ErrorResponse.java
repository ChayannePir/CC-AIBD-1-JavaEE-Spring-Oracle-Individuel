package com.medical.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe représentant une réponse d'erreur standardisée.
 * Utilisée par le GlobalExceptionHandler pour les réponses d'erreur.
 *
 * Attributs:
 * - timestamp: Date et heure de l'erreur
 * - status: Code HTTP d'erreur
 * - error: Type d'erreur
 * - message: Message d'erreur détaillé
 * - path: Chemin de la requête qui a provoqué l'erreur
 *
 * Format JSON:
 * {
 *   "timestamp": "2024-07-17T14:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Patient avec l'ID 123 non trouvé",
 *   "path": "/api/patients/123"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Réponse d'erreur standardisée")
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Timestamp de l'erreur au format ISO 8601
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Date et heure de l'erreur", example = "2024-07-17T14:30:00")
    private LocalDateTime timestamp;

    /**
     * Code HTTP d'erreur (ex: 404, 400, 500)
     */
    @Schema(description = "Code HTTP", example = "404")
    private Integer status;

    /**
     * Type d'erreur (ex: "Not Found", "Bad Request", "Internal Server Error")
     */
    @Schema(description = "Type d'erreur", example = "Not Found")
    private String error;

    /**
     * Message d'erreur détaillé
     */
    @Schema(description = "Message d'erreur détaillé", example = "Patient avec l'ID 123 non trouvé")
    private String message;

    /**
     * Chemin de la requête qui a provoqué l'erreur
     */
    @Schema(description = "Chemin de la requête", example = "/api/patients/123")
    private String path;
}

