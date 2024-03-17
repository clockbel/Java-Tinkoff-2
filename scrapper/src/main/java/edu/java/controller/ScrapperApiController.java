package edu.java.controller;

import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.jdbc.JdbcChatService;
import edu.java.service.jdbc.JdbcLinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperApiController implements ScrapperApi {
    private final ChatService chatService;
    private final LinkService linkService;

    @Autowired
    public ScrapperApiController(JdbcChatService chatService, JdbcLinkService linkService) {
        this.chatService = chatService;
        this.linkService = linkService;
    }

    @Override
    public void tgChatIdPost(@Min(1) @PathVariable Long id) {
        chatService.register(id);
    }

    @Override
    public void tgChatIdDelete(@Min(1) @PathVariable Long id) {
        chatService.unregister(id);
    }

    @Override
    public LinkResponse linksDelete(
        @Min(1) @RequestHeader("Tg-Chat-Id") Long id,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.remove(id, removeLinkRequest);
    }

    @Override
    public ListLinksResponse linksGet(@Min(1) @RequestHeader("Tg-Chat-Id") Long id) {
        return linkService.getAll(id);
    }

    @Override
    public LinkResponse linksPost(
        @Min(1) @RequestHeader("Tg-Chat-Id") Long id,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        return linkService.add(id, addLinkRequest);
    }
}

