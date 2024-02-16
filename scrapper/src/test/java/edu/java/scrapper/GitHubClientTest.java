package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.GitHubClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GitHubClientTest {

    private WireMockServer wireMockServer;
    private GitHubClient githubClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        WebClient webClient = WebClient.builder()
            .baseUrl(wireMockServer.baseUrl())
            .build();

        githubClient = new GitHubClient(webClient);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchRepository() {
        // Настройка фиктивного ответа сервера
        String responseBody = "{\"id\":  123, \"name\": \"test-repo\", \"full_name\": \"test-user/test-repo\"}";
        wireMockServer.stubFor(get(urlEqualTo("/repos/test-user/test-repo"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        // Выполнение теста
        StepVerifier.create(githubClient.fetchRepository("test-user", "test-repo"))
            .assertNext(response -> {
                Assertions.assertEquals("test-user/test-repo", response.getFullName());
            })
            .verifyComplete();
    }
}
