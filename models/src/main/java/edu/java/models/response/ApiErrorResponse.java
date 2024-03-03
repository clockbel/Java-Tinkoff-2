package edu.java.models.response;

import java.util.List;

public record ApiErrorResponse(String description,
                               String code,
                               String exceptionName,
                               String exceptionMessage,
                               List<String> stacktrace) {
    @Override
    public String toString() {
        return description + "\n" + code + "\n" + exceptionName + "\n" + exceptionMessage + "\n";
    }
}
