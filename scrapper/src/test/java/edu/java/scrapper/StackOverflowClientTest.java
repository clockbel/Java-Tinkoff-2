package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;


public class StackOverflowClientTest {
    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        WebClient webClient = WebClient.builder()
            .baseUrl(wireMockServer.baseUrl())
            .build();

        stackOverflowClient = new StackOverflowClient(webClient);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchRepository() {
        // Настройка фиктивного ответа сервера
        String questionBody = "{\"items\": [{\"question_id\":  10111}]}";
        wireMockServer.stubFor(get(urlEqualTo("/questions/10111?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(questionBody)));

        // Выполнение теста
        StepVerifier.create(stackOverflowClient.fetchQuestion(10111))
            .assertNext(response -> {
                Assertions.assertEquals(10111, response.items().get(0).questionId());
            })
            .verifyComplete();
    }
}

