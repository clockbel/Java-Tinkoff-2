package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.GitHubClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Paths;
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
    @SneakyThrows
    public void testFetchRepository() {
        // Настройка фиктивного ответа сервера
        wireMockServer.stubFor(get(urlEqualTo("/repos/clockbel/Java-Tinkoff"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(Files.readString(Paths.get("src/test/resources/github-api.json")))));

        // Выполнение теста
        StepVerifier.create(githubClient.fetchRepository("clockbel", "Java-Tinkoff"))
            .assertNext(response -> {
                Assertions.assertEquals("clockbel/Java-Tinkoff", response.getFullName());
            })
            .verifyComplete();
    }
}
