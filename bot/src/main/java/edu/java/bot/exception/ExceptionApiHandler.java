package edu.java.bot.exception;

import edu.java.bot.exception.errors.NotFoundIdChatException;
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
            Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundIdChatException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundIdChatException(NotFoundIdChatException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Чат с заданным id не найден",
            HttpStatus.NOT_FOUND.toString(),
            "Not Found Id Chat",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
