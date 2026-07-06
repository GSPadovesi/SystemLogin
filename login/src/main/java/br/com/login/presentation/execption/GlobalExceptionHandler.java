package br.com.login.presentation.execption;

import br.com.login.infrastructure.exceptions.TokenGenerationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<ApiError> handleTokenGenerationException(TokenGenerationException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(new ApiError(
                        Instant.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        exception.getMessage()
                ));
    }

    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message
    ) {
    }
}
