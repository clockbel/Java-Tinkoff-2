package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.StackOverflowClient;
import edu.java.response.stackoverflow.AnswerResponse;
import edu.java.response.stackoverflow.OwnerInformation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
    public void testFetchQuestion() {
        // Настройка фиктивного ответа сервера
        wireMockServer.stubFor(get(urlEqualTo("/questions/1566516?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(Files.readString(Paths.get("src/test/resources/stack-question-api.json")))));

        // Выполнение теста
        StepVerifier.create(stackOverflowClient.fetchQuestion(1566516))
            .assertNext(question -> {
                Assertions.assertEquals(1566516, question.items().get(0).getQuestionId());
                Assertions.assertEquals(2, question.items().get(0).getAnswerCount());
                Assertions.assertEquals(1413450759, question.items().get(0).getLastActivityDate().toEpochSecond());
                Assertions.assertEquals(1255529629, question.items().get(0).getCreationDate().toEpochSecond());
                Assertions.assertEquals(1566516, question.items().get(0).getQuestionId());
            })
            .verifyComplete();
    }
    @Test
    @SneakyThrows
    public void testFetchAnswer() {
        // Настройка фиктивного ответа сервера
        wireMockServer.stubFor(get(urlEqualTo("/questions/1566516/answers?order=desc&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(Files.readString(Paths.get("src/test/resources/stack-answer-api.json")))));

        List<AnswerResponse.AnswerInformation> informationList = stackOverflowClient.fetchAnswer(1566516).block().items();

        // Выполнение теста
        StepVerifier.create(stackOverflowClient.fetchAnswer(1566516))
            .assertNext(answer -> {
                Assertions.assertEquals(1413450759, informationList.get(0).getLastActivityDate().toEpochSecond());
                Assertions.assertEquals(1413449328, informationList.get(0).getCreationDate().toEpochSecond());
                Assertions.assertEquals(1566516, informationList.get(0).getQuestionId());
                Assertions.assertEquals(26400027, informationList.get(0).getAnswerId());
            })
            .verifyComplete();
    }
    @Test
    @SneakyThrows
    public void testFetchOwnerInfo() {
        // Настройка фиктивного ответа сервера
        wireMockServer.stubFor(get(urlEqualTo("/questions/1566516?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(Files.readString(Paths.get("src/test/resources/stack-question-api.json")))));

        OwnerInformation ownerInformation = stackOverflowClient.fetchQuestion(1566516).block().items().get(0).getOwner();
        // Выполнение теста
        StepVerifier.create(stackOverflowClient.fetchQuestion(1566516))
            .assertNext(answer -> {
                Assertions.assertEquals(47666, ownerInformation.getAccountId());
                Assertions.assertEquals("https://stackoverflow.com/users/141290/rnnbrwn", ownerInformation.getLink());
                Assertions.assertEquals(141290, ownerInformation.getUserId());
                Assertions.assertEquals("rnnbrwn", ownerInformation.getDisplayName());
            })
            .verifyComplete();
    }
}

