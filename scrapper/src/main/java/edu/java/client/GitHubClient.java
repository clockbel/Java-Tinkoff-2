package edu.java.client;

import edu.java.dto.response.github.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GitHubClient {

    private final WebClient webClient;
    private final static String DEFAULT_URL = "https://api.github.com";

    public static GitHubClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl == null ? DEFAULT_URL : baseUrl)
            .build();
        return new GitHubClient(webClient);
    }

    public Mono<RepositoryResponse> fetchRepository(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }
}
