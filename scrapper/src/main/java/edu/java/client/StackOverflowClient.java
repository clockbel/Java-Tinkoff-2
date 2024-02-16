package edu.java.client;

import edu.java.response.QuestionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<QuestionResponse> fetchQuestion(int questionId) {
        return webClient.get()
            .uri("/questions/{id}?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }
}
