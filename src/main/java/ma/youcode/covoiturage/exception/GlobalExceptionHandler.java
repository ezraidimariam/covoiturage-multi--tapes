package ma.youcode.covoiturage.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, String> response = Map.of(
                "error", "Conflict",
                "message", "Cette donnée existe déjà."
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}