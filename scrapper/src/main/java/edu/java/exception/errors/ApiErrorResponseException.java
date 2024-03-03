package edu.java.exception.errors;

import edu.java.models.response.ApiErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponseException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;
}
