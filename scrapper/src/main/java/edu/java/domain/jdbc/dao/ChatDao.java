package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.domain.jdbc.model.chat.ChatRow;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatDao {
    private final JdbcClient jdbcClient;
    private final ChatRow chatRow;

    public List<Chat> getAll() {
        String sql = "SELECT * FROM chats";
        return jdbcClient.sql(sql)
            .query(chatRow).list();
    }

    public Optional<Chat> getById(long id) {
        String sql = "SELECT * FROM chats WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatRow).optional();
    }

    public int save(Chat chat) {
        String sql = "INSERT INTO chats(id, created_at) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chat.getId(), chat.getCreatedAt())
            .update();
    }

    public int delete(long id) {
        String sql = "DELETE FROM chats WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .update();
    }
}
