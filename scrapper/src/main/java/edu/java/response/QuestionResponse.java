package edu.java.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QuestionResponse {
    private List<ItemResponse> items;

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }

    public static class ItemResponse {
        @JsonProperty("question_id")
        private long questionId;

        public long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(long questionId) {
            this.questionId = questionId;
        }
    }
}


