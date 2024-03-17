package edu.java.bot.service;

import edu.java.bot.client.ScrapperClient;
import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {
    private final ScrapperClient scrapperClient;

    public void registerChat(long id) {
        scrapperClient.registerChat(id);
    }

    public void deleteChat(long id) {
        scrapperClient.deleteChat(id);
    }

    public void getLinks(long id) {
        scrapperClient.getLinks(id);
    }

    public void addLink(long id, String link) {
        scrapperClient.addLink(id, new AddLinkRequest(URI.create(link)));
    }

    public void deleteLink(long id, String link) {
        scrapperClient.deleteLink(id, new RemoveLinkRequest(URI.create(link)));
    }
}
