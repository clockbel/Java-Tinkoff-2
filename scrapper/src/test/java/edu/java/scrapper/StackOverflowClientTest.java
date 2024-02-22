package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.StackOverflowClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @SneakyThrows
    public void testFetchQuestions() {
        // Настройка фиктивного ответа сервера
        wireMockServer.stubFor(get(urlEqualTo("/questions/1566516?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(Files.readString(Paths.get("src/test/resources/stack-overflow-api.json")))));

        // Выполнение теста
        StepVerifier.create(stackOverflowClient.fetchQuestion(1566516))
            .assertNext(question -> {
                Assertions.assertEquals(1566516, question.items().get(0).getQuestionId());
            })
            .verifyComplete();
    }
}

