package edu.java.service;

import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;

public interface LinkService {
    String EX_CHAT = "id = ";
    String EX_LINK = "url = ";

    ListLinksResponse getAll(long id);

    LinkResponse add(long id, AddLinkRequest linkRequest);

    LinkResponse remove(long id, RemoveLinkRequest linkRequest);

    default String toExMsg(String ex, String value) {
        return ex + value;
    }
}
