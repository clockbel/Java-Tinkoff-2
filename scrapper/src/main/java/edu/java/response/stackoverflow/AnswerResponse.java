package edu.java.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record AnswerResponse(
    List<AnswerInformation> items) {
    @Getter
    @Setter
    public static class AnswerInformation {
        @JsonProperty("owner")
        private OwnerInformation owner;

        @JsonProperty("last_activity_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime lastActivityDate;

        @JsonProperty("creation_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime creationDate;

        @JsonProperty("answer_id")
        private long answerId;

        @JsonProperty("question_id")
        private long questionId;
    }
}
