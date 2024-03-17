package edu.java.bot.client;

import edu.java.bot.exception.errors.ApiErrorResponseException;
import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.ApiErrorResponse;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ScrapperClient {
    private static final String LINKS = "/links";
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public static ScrapperClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl == null ? BASE_URL : baseUrl)
            .build();
        return new ScrapperClient(webClient);
    }

    public void registerChat(long id) {
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("tg-chat/{id}").build(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
                .bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse))))
            .bodyToMono(Void.class).block();
    }

    public void deleteChat(long id) {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path("/tg-chat/{id}").build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse)))
            )
            .bodyToMono(Void.class).block();
    }

    public ListLinksResponse getLinks(long id) {
        return webClient
            .get()
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse)))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorResponseException(apiErrorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
