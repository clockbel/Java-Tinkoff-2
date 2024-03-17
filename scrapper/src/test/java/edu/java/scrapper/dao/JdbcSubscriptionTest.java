package edu.java.scrapper.dao;

import java.util.ArrayList;
import java.util.List;
import edu.java.domain.jdbc.dao.SubscriptionDao;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.domain.jdbc.model.subscription.SubscriptionWithUrl;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.UtilityDataBase.createSubscription;
import static edu.java.scrapper.UtilityDataBase.getAllFromSubscription;
import static edu.java.scrapper.UtilityDataBase.getIdFromChatByTgChatId;
import static edu.java.scrapper.UtilityDataBase.getIdFromLinkByUrl;
import static edu.java.scrapper.UtilityDataBase.insertRowIntoChat;
import static edu.java.scrapper.UtilityDataBase.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JdbcSubscriptionTest extends IntegrationTest {
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    public JdbcClient jdbcClient;

    private final String saveSQL = "INSERT INTO subscription(id_chat, id_link) VALUES (%d, %d)";
    private final String defaultUrl = "defaultUrl";
    private final long defaultTgChatId = 10;

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromSubscription(jdbcClient).isEmpty());
    }

    @Test
    @DisplayName("getByLinkId (В таблице chat_link 1 значение с искомой ссылкой)")
    void getByLinkIdWhenOneChat() {
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<Subscription> contentByLinkId = subscriptionDao.getByLinkId(linkId);
        assertAll(
            () -> assertEquals(1, contentByLinkId.size()),
            () -> assertEquals(contentByLinkId.get(0).getIdChat(), chatId)
        );
    }

    @Test
    @DisplayName("getByLinkId (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdWhenManyChats() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        insertRowIntoLink(jdbcClient, defaultUrl);
        linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(jdbcClient, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(jdbcClient, defaultTgChatId + i).orElseThrow());
        }

        for (long chatId : chatIds) {
            jdbcClient.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }

        List<Subscription> contentByLinkId = subscriptionDao.getByLinkId(linkId);
        assertEquals(chatIds.size(), contentByLinkId.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), contentByLinkId.get(i).getIdChat());
        }
    }
    @Test
    void getByChatId() {
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<Subscription> contentByChatId = subscriptionDao.getByChatId(chatId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(contentByChatId.get(0).getIdLink(), linkId)
        );
    }

    @Test
    void getByChatIdJoinLink() {
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<SubscriptionWithUrl> contentByChatId = subscriptionDao.getByChatIdJoinLink(chatId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(contentByChatId.get(0).getIdLink(), linkId),
            () -> assertEquals(contentByChatId.get(0).getUrl(), defaultUrl)
        );
    }

    @Test
    void getAll() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        insertRowIntoLink(jdbcClient, defaultUrl);
        linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(jdbcClient, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(jdbcClient, defaultTgChatId + i).orElseThrow());
        }
        for (long chatId : chatIds) {
            jdbcClient.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }
        List<Subscription> content = subscriptionDao.getAll();
        assertEquals(chatIds.size(), content.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), content.get(i).getIdChat());
            assertEquals(linkId, content.get(i).getIdLink());
        }
    }

    @Test
    void save() {
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        subscriptionDao.save(createSubscription(chatId, linkId));
        List<Subscription> content = getAllFromSubscription(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.get(0).getIdLink(), linkId),
            () -> assertEquals(content.get(0).getIdChat(), chatId)
        );
    }

    @Test
    void remove() {
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();
        List<Subscription> content = getAllFromSubscription(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.get(0).getIdLink(), linkId),
            () -> assertEquals(content.get(0).getIdChat(), chatId)
        );
        subscriptionDao.delete(chatId, linkId);
        List<Subscription> actualContent = getAllFromSubscription(jdbcClient);
        assertTrue(actualContent.isEmpty());
    }
}
