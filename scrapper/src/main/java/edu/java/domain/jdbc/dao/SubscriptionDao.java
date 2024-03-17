package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.domain.jdbc.model.subscription.SubscriptionRow;
import edu.java.domain.jdbc.model.subscription.SubscriptionWithUrl;
import edu.java.domain.jdbc.model.subscription.SubscriptionWithUrlRow;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscriptionDao {
    private final JdbcClient jdbcClient;
    private final SubscriptionRow chatLinkRowMapper;
    private final SubscriptionWithUrlRow chatLinkWithUrlRowMapper;

    public List<Subscription> getAll() {
        String sql = "SELECT * FROM subscription";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    public List<Subscription> getByChatId(long id) {
        String sql = "SELECT * FROM subscription WHERE id_chat = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<SubscriptionWithUrl> getByChatIdJoinLink(long id) {
        String sql = """
            SELECT cl.id_chat, cl.id_link, l.url
            FROM subscription cl
            JOIN links l ON l.id = cl.id_link
            WHERE id_chat = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithUrlRowMapper).list();
    }

    public List<Subscription> getByLinkId(long id) {
        String sql = "SELECT * FROM subscription WHERE id_link = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public int save(Subscription chatLink) {
        String sql = "INSERT INTO subscription(id_chat, id_link) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getIdChat(), chatLink.getIdLink())
            .update();
    }

    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM subscription WHERE id_chat = ? AND id_link = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
