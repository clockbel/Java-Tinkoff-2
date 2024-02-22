package edu.java.client;

import edu.java.response.stackoverflow.QuestionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<QuestionResponse> fetchQuestion(long questionId) {
        return webClient.get()
            .uri("/questions/{question}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }

    public Mono<QuestionResponse> fetchAnswer(long questionId) {
        return webClient.get()
            .uri("/questions/{question}/answers?order=desc&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }
}
