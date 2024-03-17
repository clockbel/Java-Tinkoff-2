package edu.java.scrapper.dao;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.model.link.Link;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.UtilityDataBase.createLink;
import static edu.java.scrapper.UtilityDataBase.getAllFromLink;
import static edu.java.scrapper.UtilityDataBase.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JdbcLinkTest extends IntegrationTest {
    @Autowired
    private LinkDao linkDao;
    @Autowired
    public JdbcClient jdbcClient;

    private final String defaultUrl = "defaultUrl";

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromLink(jdbcClient).isEmpty());
    }

    @Test
    void getByUrl() {
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.get(0).getUrl(), defaultUrl)
        );
        Optional<Link> link = linkDao.getByUrl(defaultUrl);
        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(link.get().getUrl(), defaultUrl)
        );
    }

    @Test
    void getById() {
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.get(0).getUrl(), defaultUrl)
        );
        long id = actualLinks.get(0).getId();
        Optional<Link> link = linkDao.getById(id);
        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(link.get().getUrl(), defaultUrl)
        );
    }

    @Test
    void getByLustUpdate() {
        OffsetDateTime now = OffsetDateTime.now();
        String sql = "INSERT INTO links(url, last_updated) VALUES (?, ?)";
        long count = 10;
        for (long id = 1; id <= 10; id++) {
            jdbcClient.sql(sql)
                .params(defaultUrl + id, now.plusSeconds(5 * id))
                .update();
        }

        List<Link> actualLinks = linkDao.getByLustUpdate(now.plusSeconds(25));
        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.size(), count/2 - 1)
        );
    }

    @Test
    void getAll() {
        long count = 10;
        for (long id = 1; id < 10; id++) {
            insertRowIntoLink(jdbcClient, defaultUrl + id);
        }

        List<Link> actualLinks = linkDao.getAll();
        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.size(), count - 1)
        );
    }

    @Test
    void save() {
        linkDao.save(createLink(defaultUrl));

        List<Link> actualLinkList = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(actualLinkList.isEmpty()),
            () -> assertEquals(actualLinkList.get(0).getUrl(), defaultUrl)
        );
    }

    @Test
    void updateLastUpdateAtById() {
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.get(0).getUrl(), defaultUrl)
        );
        linkDao.updateLastUpdateAtById(links.get(0).getId(), OffsetDateTime.MAX);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertEquals(actualLinks.get(0).getLastUpdateAt(), OffsetDateTime.MAX);
    }

    @Test
    void deleteByUrl() {
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.get(0).getUrl(), defaultUrl)
        );
        linkDao.deleteByUrl(defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertTrue(actualLinks.isEmpty());
    }

    @Test
    void deleteById() {
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.get(0).getUrl(), defaultUrl)
        );
        linkDao.deleteById(links.get(0).getId());
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertTrue(actualLinks.isEmpty());
    }
}
