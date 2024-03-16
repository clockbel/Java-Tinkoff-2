package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContainerBaseTest extends IntegrationTest {
    @Test
    void test() {
//        List<Object> tableNames = jdbcClient.sql(
//                "SELECT table_name FROM information_schema.tables " +
//                    "WHERE table_schema='public' " +
//                    "AND table_catalog = current_database()")
//            .query()
//            .singleColumn();
    }
}
