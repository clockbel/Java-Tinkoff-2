package edu.java.scrapper;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.domain.jdbc.model.chat.ChatRow;
import edu.java.domain.jdbc.model.link.Link;
import edu.java.domain.jdbc.model.link.LinkRow;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.domain.jdbc.model.subscription.SubscriptionRow;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.simple.JdbcClient;

@UtilityClass
public class UtilityDataBase {
    private final ChatRow chatRowMapper = new ChatRow();
    private final LinkRow linkRowMapper = new LinkRow();
    private final SubscriptionRow chatLinkRow = new SubscriptionRow();
    private final String getAllFromChat = "SELECT * FROM chats";
    private final String getAllFromLink = "SELECT * FROM links";
    private final String getAllFromSubscription = "SELECT * FROM subscription";

    public static List<Chat> getAllFromChat(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromChat)
            .query(chatRowMapper)
            .list();
    }

    public static List<Link> getAllFromLink(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromLink)
            .query(linkRowMapper)
            .list();
    }

    public static List<Subscription> getAllFromSubscription(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromSubscription)
            .query(chatLinkRow)
            .list();
    }

    public static Optional<Long> getIdFromChatByTgChatId(JdbcClient jdbcClient, long id) {
        return jdbcClient.sql("SELECT (id) FROM chats WHERE id = ?")
            .param(id)
            .query(Long.class).optional();
    }

    public static Optional<Long> getIdFromLinkByUrl(JdbcClient jdbcClient, String url) {
        return jdbcClient.sql("SELECT (id) FROM links WHERE url = ?")
            .param(url)
            .query(Long.class).optional();
    }

    public static void insertRowIntoChat(JdbcClient jdbcClient, long id) {
        jdbcClient.sql("INSERT INTO chats(id, created_at) VALUES (?, CURRENT_TIMESTAMP)")
            .param(id)
            .update();
    }

    public static void insertRowIntoLink(JdbcClient jdbcClient, String url) {
        jdbcClient.sql(
                "INSERT INTO links(url, last_updated) VALUES (?, CURRENT_TIMESTAMP)")
            .param(url)
            .update();
    }

    public static Chat createChat(long id) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setCreatedAt(OffsetDateTime.now());
        return chat;
    }

    public static Link createLink(String url) {
        Link link = new Link();
        link.setUrl(url);
        link.setLastUpdateAt(OffsetDateTime.now());
        return link;
    }

    public static Subscription createSubscription(long idChat, long idLink) {
        Subscription chatLink = new Subscription();
        chatLink.setIdChat(idChat);
        chatLink.setIdLink(idLink);
        return chatLink;
    }
}
