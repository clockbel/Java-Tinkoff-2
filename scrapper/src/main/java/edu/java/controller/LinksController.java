package edu.java.controller;

import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinksController implements LinksApi {
    private final String defaultLink = "aa";
    private final Long defaultId = 1L;

    @Override
    public ResponseEntity<LinkResponse> linksDelete(
        @Min(1) @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(defaultId, URI.create(defaultLink));
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(@Min(1) @RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkResponse> linkResponses = new ArrayList<>();
        ListLinksResponse links = new ListLinksResponse(linkResponses, linkResponses.size());
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(
        @Min(1) @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(defaultId, URI.create(defaultLink));
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
