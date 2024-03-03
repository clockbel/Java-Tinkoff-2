package edu.java.bot.exception.errors;

import edu.java.models.response.ApiErrorResponse;

public class NotFoundIdChatException extends RuntimeException {
    public NotFoundIdChatException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
    }
}
