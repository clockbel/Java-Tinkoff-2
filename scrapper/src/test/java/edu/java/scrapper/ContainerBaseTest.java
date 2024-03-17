package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class ContainerBaseTest extends IntegrationTest {
    @Autowired
    JdbcClient jdbcClient;

    @Test
    void createAllTables() {
        String sql = "SELECT table_name FROM scrapper.information_schema.tables";
        var result = jdbcClient.sql(sql)
            .query()
            .singleColumn();
        assertAll(
            () -> Assertions.assertTrue(result.contains("chats")),
            () -> Assertions.assertTrue(result.contains("links")),
            () -> Assertions.assertTrue(result.contains("subscription"))
        );
    }

    @Test
    void createColumnsInChatTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("chats"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertTrue(columns.contains("id")),
            () -> Assertions.assertTrue(columns.contains("created_at"))
        );
    }

    @Test
    void createColumnsInLinkTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("links"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertTrue(columns.contains("id")),
            () -> Assertions.assertTrue(columns.contains("url")),
            () -> Assertions.assertTrue(columns.contains("last_updated"))
        );
    }

    @Test
    void createColumnsInChatLinkTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("subscription"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertTrue(columns.contains("id_chat")),
            () -> Assertions.assertTrue(columns.contains("id_link"))
        );
    }
}
