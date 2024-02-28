package edu.java.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record QuestionResponse(
    List<QuestionInformation> items) {
    @Getter
    @Setter
    public static class QuestionInformation {
        @JsonIgnore
        private List<AnswerResponse.AnswerInformation> answers;
        @JsonProperty("owner")
        private OwnerInformation owner;
        @JsonProperty("answer_count")
        private int answerCount;
        @JsonProperty("last_activity_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime lastActivityDate;
        @JsonProperty("creation_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private OffsetDateTime creationDate;
        @JsonProperty("question_id")
        private long questionId;
    }
}


