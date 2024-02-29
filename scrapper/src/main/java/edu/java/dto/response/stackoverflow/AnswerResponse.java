package edu.java.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record AnswerResponse(
    List<AnswerInformation> items) {
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class AnswerInformation {
        private OwnerInformation owner;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime lastActivityDate;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime creationDate;
        private long answerId;
        private long questionId;
    }
}
