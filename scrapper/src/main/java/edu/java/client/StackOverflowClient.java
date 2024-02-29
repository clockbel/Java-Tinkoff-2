package edu.java.client;

import edu.java.dto.response.stackoverflow.AnswerResponse;
import edu.java.dto.response.stackoverflow.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StackOverflowClient {

    private final WebClient webClient;
    private final static String DEFAULT_URL = "https://api.stackexchange.com/2.3";

    public static StackOverflowClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl == null ? DEFAULT_URL : baseUrl)
            .build();
        return new StackOverflowClient(webClient);
    }

    public Mono<QuestionResponse> fetchQuestion(long questionId) {
        return webClient.get()
            .uri("/questions/{question}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }

    public Mono<AnswerResponse> fetchAnswer(long questionId) {
        return webClient.get()
            .uri("/questions/{question}/answers?order=desc&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(AnswerResponse.class);
    }
}
