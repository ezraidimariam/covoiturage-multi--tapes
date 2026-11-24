package ma.youcode.covoiturage.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatus(
            ResponseStatusException exception,
            HttpServletRequest request) {
        if (exception.getStatusCode().is4xxClientError()) {
            log.warn("Requête refusée {} {}: {}", request.getMethod(),
                    request.getRequestURI(), exception.getReason());
        }
        return problem(exception.getStatusCode(), exception.getReason(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .reduce((first, next) -> first + "; " + next)
                .orElse("Les données fournies sont invalides");
        log.warn("Validation refusée {} {}: {}", request.getMethod(), request.getRequestURI(), message);
        return problem(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ProblemDetail> handleInvalidRequest(
            Exception exception,
            HttpServletRequest request) {
        String message = exception instanceof ConstraintViolationException
                ? exception.getMessage()
                : "Le corps de la requête est invalide";
        return problem(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        log.warn("Conflit de données {} {}", request.getMethod(), request.getRequestURI());
        return problem(HttpStatus.CONFLICT, "Cette donnée existe déjà ou est utilisée.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        log.error("Erreur interne sur {} {}", request.getMethod(), request.getRequestURI(), exception);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue.", request);
    }

    private ResponseEntity<ProblemDetail> problem(
            HttpStatusCode status,
            String message,
            HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(status.value());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status,
                message == null || message.isBlank() ? httpStatus.getReasonPhrase() : message);
        detail.setTitle(httpStatus.getReasonPhrase());
        detail.setInstance(URI.create(request.getRequestURI()));
        detail.setProperty("timestamp", OffsetDateTime.now());
        detail.setProperty("code", status.value());
        detail.setProperty("error", httpStatus.name());
        return ResponseEntity.status(status).body(detail);
    }
}
