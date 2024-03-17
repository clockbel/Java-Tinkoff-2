package edu.java.scrapper.controllerTest;

import edu.java.controller.ScrapperApiController;
import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import java.net.URI;

@SpringBootTest
public class ScrapperControllerTest {
    private final WebTestClient webTestClient;
    private final URI defaultLink = URI.create("a");
    private final long defaultId = 1L;

    @Autowired
    public ScrapperControllerTest(ScrapperApiController scrapperApiController) {
        webTestClient = WebTestClient.bindToController(scrapperApiController).build();
    }

    @Test
    @DisplayName("Регистрация чата, корректные данные")
    void registerChat() {
        webTestClient.post()
            .uri("/tg-chat/{id}", 1L)
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Удаление чата, корректные данные")
    void deleteChat() {
        webTestClient.delete()
            .uri("/tg-chat/{id}", 1L)
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Регистрация чата, не корректные данные ID")
    void registerChatWhenInvalidId() {
        webTestClient.post()
            .uri("/tg-chat/{id}", -1L)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Удаление чата, не корректные данные ID")
    void deleteChatWhenInvalidId() {
        webTestClient.delete()
            .uri("/tg-chat/{id}", -1L)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Получить все отслеживаемые ссылки, корректные данные")
    void getAllLinks() {
        webTestClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Добавить ссылку, корректные данные")
    void addLink() {
        webTestClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(defaultLink)))
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Удалить ссылку, корректные данные")
    void removeLink() {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(defaultLink)))
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Получить все отслеживаемые ссылки, не корректные данные ID")
    void getAllLinksWhenInvalidId() {
        webTestClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(-1L))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Добавить ссылку, не корректные данные")
    void addLinkWhenInvalidId() {
        webTestClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(-1L))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(defaultLink)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Добавить ссылку, ссылка null")
    void addLinkWhenLinkIsNull() {
        webTestClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(null)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Добавить ссылку, ссылка пустая")
    void addLinkWhenLinkIsEmpty() {
        webTestClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(null)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Удалаить ссылку, ссылка null")
    void removeLinkWhenLinkIsNull() {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(null)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Удалаить ссылку, ссылка пустая")
    void removeLinkWhenLinkIsEmpty() {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(null)))
            .exchange().expectStatus().isBadRequest();
    }
}
