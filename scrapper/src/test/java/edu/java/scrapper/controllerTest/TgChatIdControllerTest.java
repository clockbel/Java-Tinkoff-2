package edu.java.scrapper.controllerTest;

import edu.java.controller.TgChatApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

public class TgChatIdControllerTest {
    private final WebTestClient webTestClient = WebTestClient.bindToController(new TgChatApiController()).build();

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
}
