package edu.java.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionResponse(
    List<ItemResponse> items) {

    public record ItemResponse(
        @JsonProperty("question_id")
        long questionId) {
    }
}


