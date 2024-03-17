package edu.java.scrapper.service;

import java.time.OffsetDateTime;
import java.util.List;
import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.dao.SubscriptionDao;
import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.domain.jdbc.model.link.Link;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.exception.errors.DuplicateRegistrationException;
import edu.java.exception.errors.NotFoundIdChatException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.UtilityDataBase.createLink;
import static edu.java.scrapper.UtilityDataBase.createSubscription;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcChatServiceTest extends IntegrationTest {
    @Autowired
    private JdbcChatService chatService;
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private SubscriptionDao chatLinkDao;
    private final long tgChatId = 10;

    private Chat createChat() {
        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());
        return chat;
    }

    @Test
    @DisplayName("Регистрация чата")
    void registerChat() {
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        chatService.register(tgChatId);
        assertTrue(chatDao.getById(tgChatId).isPresent());
    }

    @Test
    @DisplayName("Повторная регистрация")
    void reRegisterChat() {
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.getById(tgChatId).isPresent());
        assertThrows(
            DuplicateRegistrationException.class,
            () -> chatService.register(tgChatId)
        );
    }

    @Test
    @DisplayName("Удаление чата")
    void unregisterChat() {
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.getById(tgChatId).isPresent());
        chatService.unregister(tgChatId);
        assertTrue(chatDao.getById(tgChatId).isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    void unregisterChatWhenChatIdNotFound() {
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        assertThrows(
            NotFoundIdChatException.class,
            () -> chatService.unregister(tgChatId)
        );
    }

    @Test
    @DisplayName("Удаление чата и всех отслеживаемых ссылок")
    void unregisterChatAndDeleteLinks() {
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        chatDao.save(createChat());
        long chatId = chatDao.getById(tgChatId).orElseThrow().getId();
        assertTrue(chatDao.getById(tgChatId).isPresent());

        String url = "url";
        linkDao.save(createLink(url));
        long linkId1 = linkDao.getByUrl(url).orElseThrow().getId();
        linkDao.save(createLink(url + url));
        long linkId2 = linkDao.getByUrl(url + url).orElseThrow().getId();

        chatLinkDao.save(createSubscription(chatId, linkId1));
        chatLinkDao.save(createSubscription(chatId, linkId2));
        List<Subscription> tempSubscription = chatLinkDao.getAll();
        assertEquals(2, tempSubscription.size());

        chatService.unregister(tgChatId);
        assertTrue(chatDao.getById(tgChatId).isEmpty());
        List<Subscription> actualSubscription = chatLinkDao.getAll();
        List<Link> actualLink = linkDao.getAll();
        assertAll(
            () -> assertTrue(chatDao.getById(tgChatId).isEmpty()),
            () -> assertEquals(0, actualSubscription.size()),
            () -> assertEquals(0, actualLink.size())
        );
    }
}
