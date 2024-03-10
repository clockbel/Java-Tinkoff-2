package edu.java.bot.exception.errors;

import edu.java.models.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.stream.Collectors;

public class NotFoundIdChatException extends RuntimeException {
    public ApiErrorResponse apiErrorResponse;

    public NotFoundIdChatException(Exception ex) {
        super(ex);
        apiErrorResponse = new ApiErrorResponse(
            "Чат с заданным id не найден",
            HttpStatus.NOT_FOUND.toString(),
            "Not Found Id Chat",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }
}
