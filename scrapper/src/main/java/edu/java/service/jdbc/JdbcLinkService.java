package edu.java.service.jdbc;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.dao.SubscriptionDao;
import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.domain.jdbc.model.link.Link;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.domain.jdbc.model.subscription.SubscriptionWithUrl;
import edu.java.exception.errors.DuplicateRegistrationException;
import edu.java.exception.errors.NotFoundIdChatException;
import edu.java.exception.errors.NotFoundLinkException;
import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcLinkService implements LinkService {
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final SubscriptionDao subscriptionDao;

    @Override
    public ListLinksResponse getAll(long id) {
        long chatId = getChatId(id).getId();

        List<SubscriptionWithUrl> chatLinksByChat = subscriptionDao.getByChatIdJoinLink(chatId);
        List<LinkResponse> linkResponses = chatLinksByChat.stream()
            .map(row -> new LinkResponse(row.getIdLink(), URI.create(row.getUrl())))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse add(long id, AddLinkRequest linkRequest) {
        String url = linkRequest.link().toString();
        long chatId = getChatId(id).getId();
        Link actualLink;

        //Создание ссылки в таблице ссылок, если ее нет
        if (linkDao.getByUrl(url).isEmpty()) {
            Link createLink = new Link();
            createLink.setUrl(url);
            createLink.setLastUpdateAt(now());
            linkDao.save(createLink);
            actualLink = linkDao.getByUrl(url).get();
        } else {
            //Иначе проверка на предмет повторного добавления
            actualLink = linkDao.getByUrl(url).get();
            for (Subscription chatLink : subscriptionDao.getByChatId(chatId)) {
                if (chatLink.getIdLink() == actualLink.getId()) {
                    throw new DuplicateRegistrationException(
                        toExMsg(EX_CHAT, String.valueOf(id))
                            + ", "
                            + toExMsg(EX_LINK, actualLink.getUrl())
                    );
                }
            }
        }

        Subscription chatLink = createSubscription(chatId, actualLink.getId());
        subscriptionDao.save(chatLink);

        return new LinkResponse(actualLink.getId(), URI.create(actualLink.getUrl()));
    }

    @Override
    public LinkResponse remove(long id, RemoveLinkRequest linkRequest) {
        String url = linkRequest.link().toString();
        Link actualLink = getLinkByUrl(url);
        long chatId = getChatId(id).getId();
        long linkId = actualLink.getId();
        int countChatTrackLink = subscriptionDao.getByLinkId(linkId).size();
        subscriptionDao.delete(chatId, linkId);
        //Если ссылку отслеживает 1 чат, удалить из таблицы ссылок
        if (countChatTrackLink == 1) {
            linkDao.deleteByUrl(url);
        }

        return new LinkResponse(actualLink.getId(), URI.create(actualLink.getUrl()));
    }

    private Subscription createSubscription(long idChat, long idLink) {
        Subscription chatLink = new Subscription();
        chatLink.setIdChat(idChat);
        chatLink.setIdLink(idLink);
        return chatLink;
    }

    private Chat getChatId(long id) {
        return chatDao.getById(id)
            .orElseThrow(
                () -> new NotFoundIdChatException(toExMsg(EX_CHAT, String.valueOf(id)))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkDao.getByUrl(url)
            .orElseThrow(
                () -> new NotFoundLinkException(toExMsg(EX_LINK, url))
            );
    }
}
