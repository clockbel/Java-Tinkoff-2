package edu.java.exception;

import edu.java.exception.errors.DuplicateLinkTrackException;
import edu.java.exception.errors.DuplicateRegistrationException;
import edu.java.exception.errors.NotFoundLinkException;
import edu.java.models.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Некорректные параметры запроса",
            HttpStatus.BAD_REQUEST.toString(),
            "Validation Error",
            ex.getMessage(),
            (Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList()))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateLinkTrackException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateLinkTrackException(DuplicateLinkTrackException ex) {
        ApiErrorResponse errorResponse =
            new ApiErrorResponse(
                "Ссылка уже добавлена",
                HttpStatus.CONFLICT.toString(),
                "Duplicate Link Track",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList())
            );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundLinkException(NotFoundLinkException ex) {
        ApiErrorResponse errorResponse =
            new ApiErrorResponse(
                "Ссылка не найдена",
                HttpStatus.NOT_FOUND.toString(),
                "Not Found Link",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList())
            );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateRegistrationException(DuplicateRegistrationException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Чат уже зарегистрирован",
            HttpStatus.CONFLICT.toString(),
            "Duplicate Registration",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
