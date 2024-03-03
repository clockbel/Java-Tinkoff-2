package controllerTest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import edu.java.bot.controller.UpdateController;
import edu.java.models.request.LinkUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class BotControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToController(new UpdateController()).build();
    private final Long defaultId = 1L;
    private final URI defaultUrl = URI.create("aa");
    private final String defaultDescription = "aaa";
    private final List<Long> defaultList = List.of(1L, 2L);

    private LinkUpdateRequest getLinkUpdateRequest(long id, URI url, String description, List<Long> tgChatIds) {
        return new LinkUpdateRequest(id, url, description, tgChatIds);
    }

    private WebTestClient.RequestHeadersSpec<?> createPostResponse(LinkUpdateRequest linkUpdateRequest) {
        return webTestClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(linkUpdateRequest));
    }

    @Test
    @DisplayName("Корректные данные")
    public void sendUpdateValidId() {
        LinkUpdateRequest linkUpdateRequest =
            getLinkUpdateRequest(defaultId, defaultUrl, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Список tgChatId пуст")
    public void sendUpdateTgChatIdsIsEmpty() {
        LinkUpdateRequest linkUpdateRequest =
            getLinkUpdateRequest(defaultId, defaultUrl, defaultDescription, new ArrayList<>());
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("id incorrect")
    public void sendUpdateWhenIdLessThenOne() {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(-1L, defaultUrl, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("URL incorrect")
    public void sendUpdateWhenUrlIsNull() {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, null, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("description incorrect")
    public void sendUpdateWhenDescriptionIsNull() {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, defaultUrl, null, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("tgChatIds - null")
    public void sendUpdateWhenTgChatIdsIsNull() {
        LinkUpdateRequest linkUpdateRequest =
            getLinkUpdateRequest(defaultId, defaultUrl, defaultDescription, null);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }
}
