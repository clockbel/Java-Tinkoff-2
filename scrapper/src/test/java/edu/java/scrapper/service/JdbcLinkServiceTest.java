package edu.java.scrapper.service;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.dao.SubscriptionDao;
import edu.java.domain.jdbc.model.link.Link;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.exception.errors.DuplicateRegistrationException;
import edu.java.exception.errors.NotFoundIdChatException;
import edu.java.models.request.AddLinkRequest;
import edu.java.models.request.RemoveLinkRequest;
import edu.java.models.response.LinkResponse;
import edu.java.models.response.ListLinksResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import static edu.java.scrapper.UtilityDataBase.createChat;
import static edu.java.scrapper.UtilityDataBase.createLink;
import static edu.java.scrapper.UtilityDataBase.createSubscription;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    JdbcLinkService linkService;
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private SubscriptionDao SubscriptionDao;
    private final long id = 10;
    private final String url = "url";

    @Test
    @DisplayName("Получить все ссылки пользователя")
    void getAll() {
        //Заполнение таблиц
        short countLinks = 8;
        chatDao.save(createChat(id));
        long chatId = chatDao.getById(id).orElseThrow().getId();
        for (int i = 0; i < countLinks; i++) {
            String tempUrl = url + i;
            linkDao.save(createLink(tempUrl));
            long linkId = linkDao.getByUrl(tempUrl).orElseThrow().getId();
            SubscriptionDao.save(createSubscription(chatId, linkId));
        }
        ListLinksResponse response = linkService.getAll(id);
        assertEquals(response.size(), countLinks);
        for (int i = 0; i < countLinks; i++) {
            assertEquals(url + i, response.links().get(i).url().toString());
        }
    }

    @Test
    @DisplayName("Получить все ссылки пользователя, неверный chatId")
    void getAllWhenChatIdNotFound() {
        //Заполнение таблиц
        chatDao.save(createChat(id));
        long chatId = chatDao.getById(id).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        SubscriptionDao.save(createSubscription(chatId, linkId));

        assertThrows(
            NotFoundIdChatException.class,
            () -> linkService.getAll(id + 1)
        );
    }

    @Test
    @DisplayName("Добавить ссылку")
    void add() {
        //Заполнение таблиц
        chatDao.save(createChat(id));
        long chatId = chatDao.getById(id).orElseThrow().getId();
        LinkResponse response = linkService.add(id, new AddLinkRequest(URI.create(url)));

        Subscription actualSubscription = SubscriptionDao.getByChatId(chatId).get(0);
        Link actualLink = linkDao.getByUrl(url).orElseThrow();

        assertAll(
            () -> assertEquals(url, response.url().toString()),
            () -> assertEquals(url, actualLink.getUrl()),
            () -> assertEquals(actualLink.getId(), actualSubscription.getIdLink()),
            () -> assertEquals(chatId, actualSubscription.getIdChat())
        );
    }

    @Test
    @DisplayName("Добавить ссылку, неверный chatId")
    void addWhenChatIdNotFound() {
        //Заполнение таблиц
        chatDao.save(createChat(id));

        assertThrows(
            NotFoundIdChatException.class,
            () -> linkService.add(id + 1, new AddLinkRequest(URI.create(url)))
        );
    }

    @Test
    @DisplayName("Добавить ссылку, повторное добавление")
    void addWhenReAddLink() {
        //Заполнение таблиц
        chatDao.save(createChat(id));
        linkService.add(id, new AddLinkRequest(URI.create(url)));

        assertThrows(
            DuplicateRegistrationException.class,
            () -> linkService.add(id, new AddLinkRequest(URI.create(url)))
        );
    }

    @Test
    @DisplayName("Удалить ссылку, отслеживаемую одним чатом (каскадное удаление)")
    void removeWhenOneChatTrack() {
        //Заполнение таблиц
        chatDao.save(createChat(id));
        long chatId = chatDao.getById(id).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        SubscriptionDao.save(createSubscription(chatId, linkId));

        Subscription tempSubscription = SubscriptionDao.getByChatId(chatId).get(0);
        Link tempLink = linkDao.getByUrl(url).orElseThrow();
        assertAll(
            "Проверка добавления ссылки",
            () -> assertEquals(url, tempLink.getUrl()),
            () -> assertEquals(linkId, tempLink.getId()),
            () -> assertEquals(linkId, tempSubscription.getIdLink()),
            () -> assertEquals(chatId, tempSubscription.getIdChat())
        );

        linkService.remove(id, new RemoveLinkRequest(URI.create(url)));

        List<Subscription> actualSubscription = SubscriptionDao.getByChatId(chatId);
        Optional<Link> actualLink = linkDao.getByUrl(url);
        assertAll(
            "Ссылка удалена из всех таблиц",
            () -> assertEquals(0, actualSubscription.size()),
            () -> assertTrue(actualLink.isEmpty())
        );
    }

    @Test
    @DisplayName("Удалить ссылку, отслеживаемую несколькими чатами")
    void removeWhenManyChatsTrack() {
        //Заполнение таблиц
        chatDao.save(createChat(id));
        long chatId1 = chatDao.getById(id).orElseThrow().getId();
        chatDao.save(createChat(id + 1));
        long chatId2 = chatDao.getById(id + 1).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        SubscriptionDao.save(createSubscription(chatId1, linkId));
        SubscriptionDao.save(createSubscription(chatId2, linkId));

        Subscription tempSubscription1 = SubscriptionDao.getByChatId(chatId1).get(0);
        Subscription tempSubscription2 = SubscriptionDao.getByChatId(chatId2).get(0);
        Link tempLink = linkDao.getByUrl(url).orElseThrow();
        assertAll(
            "Проверка добавления ссылки",
            () -> assertEquals(url, tempLink.getUrl()),
            () -> assertEquals(linkId, tempLink.getId()),
            () -> assertEquals(linkId, tempSubscription1.getIdLink()),
            () -> assertEquals(chatId1, tempSubscription1.getIdChat()),
            () -> assertEquals(linkId, tempSubscription2.getIdLink()),
            () -> assertEquals(chatId2, tempSubscription2.getIdChat())
        );

        linkService.remove(id, new RemoveLinkRequest(URI.create(url)));

        List<Subscription> actualSubscription = SubscriptionDao.getAll();
        Optional<Link> actualLink = linkDao.getByUrl(url);
        assertAll(
            "Удалена только 1 запись о связи",
            () -> assertEquals(1, actualSubscription.size()),
            () -> assertTrue(actualLink.isPresent()),
            () -> assertEquals(url, actualLink.get().getUrl())
        );
    }
}
