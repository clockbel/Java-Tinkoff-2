package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.BotClient;
import java.net.URI;
import java.util.List;
import edu.java.models.request.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@WireMockTest(httpPort = 8090)
public class BotClientTest {

    private final BotClient botClient = BotClient.create("http://localhost:8090");

    @Test
    public void testSendUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Update processed")));
        assertDoesNotThrow(() -> botClient.sendUpdate(new LinkUpdateRequest(1L, URI.create("1"), "1", List.of(1L))));
    }
}

