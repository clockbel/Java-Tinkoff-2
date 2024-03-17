package edu.java.client;

import edu.java.exception.errors.ApiErrorResponseException;
import edu.java.models.request.LinkUpdateRequest;
import edu.java.models.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BotClient {
    private final WebClient webClient;

    public static BotClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return new BotClient(webClient);
    }

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(linkUpdateRequest
            ))
            .retrieve().onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse
                .bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse))))
            .bodyToMono(Void.class)
            .block();
    }
}
