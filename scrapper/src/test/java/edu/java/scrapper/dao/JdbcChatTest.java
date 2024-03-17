package edu.java.scrapper.dao;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static edu.java.scrapper.UtilityDataBase.createChat;
import static edu.java.scrapper.UtilityDataBase.getAllFromChat;
import static edu.java.scrapper.UtilityDataBase.insertRowIntoChat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private JdbcClient jdbcClient;

    private final long id = 10;

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromChat(jdbcClient).isEmpty());
    }

    @Test
    void getByTgChatId() {
        insertRowIntoChat(jdbcClient, id);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.get(0).getId(), id)
        );

        Optional<Chat> chat = chatDao.getById(id);
        assertAll(
            "Поддтверждение, что это только что добавленный чат",
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(chat.get().getId(), id)
        );
    }

    @Test
    void getById() {
        insertRowIntoChat(jdbcClient, id);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.get(0).getId(), id)
        );
        long id = actualChats.get(0).getId();
        Optional<Chat> chat = chatDao.getById(id);
        assertAll(
            "Поддтверждение, что это только что добавленный чат",
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(chat.get().getId(), id)
        );
    }

    @Test
    void getAll() {
        long count = 10;
        for (long chatId = 1; chatId < count; chatId++) {
            insertRowIntoChat(jdbcClient, chatId);
        }

        List<Chat> actualChats = chatDao.getAll();
        assertAll(
            "Поддтверждение, что чаты добавились",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.size(), count - 1)
        );
    }

    @Test
    void save() {
        chatDao.save(createChat(id));

        List<Chat> actualChatsList = getAllFromChat(jdbcClient);
        assertAll(
            () -> assertFalse(actualChatsList.isEmpty()),
            () -> assertEquals(actualChatsList.get(0).getId(), id)
        );
    }

    @Test
    void delete() {
        insertRowIntoChat(jdbcClient, id);
        List<Chat> chats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(chats.isEmpty()),
            () -> assertEquals(chats.get(0).getId(), id)
        );
        chatDao.delete(id);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertTrue(actualChats.isEmpty());
    }
}
