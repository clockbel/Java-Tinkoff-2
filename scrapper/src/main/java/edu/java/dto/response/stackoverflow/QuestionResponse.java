package edu.java.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record QuestionResponse(
    List<QuestionInformation> items) {
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestionInformation {
        private List<AnswerResponse.AnswerInformation> answers;
        private OwnerInformation owner;
        private int answerCount;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime lastActivityDate;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime creationDate;
        private long questionId;
    }
}


